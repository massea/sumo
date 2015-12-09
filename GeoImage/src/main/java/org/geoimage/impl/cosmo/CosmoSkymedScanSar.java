package org.geoimage.impl.cosmo;

import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.geoimage.factory.GeoTransformFactory;
import org.geoimage.impl.Gcp;
import org.slf4j.LoggerFactory;

import ncsa.hdf.object.Attribute;
import ncsa.hdf.object.HObject;
import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;
import ncsa.hdf.object.h5.H5ScalarDS;

public class CosmoSkymedScanSar extends AbstractCosmoSkymedImage {
	private static org.slf4j.Logger logger=LoggerFactory.getLogger(CosmoSkymedScanSar.class);
	private HashMap<String,List<Object>>metadataGroup=null;
	
	public CosmoSkymedScanSar(File file,String pathImg,String group){
		super(file,pathImg,group);
	}
	
	
	 @SuppressWarnings("unchecked")
		public boolean initialise() {
	        try {
	        	this.imgName=super.manifestFile.getName();
	        	this.imgName=this.imgName.substring(0, this.imgName.lastIndexOf("."));
	        	this.displayName=super.manifestFile.getName();
	        	if(group!=null&&!group.equalsIgnoreCase(""))
	        		this.displayName=this.displayName+"_"+group;
	        	
	        	h5file = new H5File(super.manifestFile.getAbsolutePath(), H5File.READ);
	        	imagedata = (H5ScalarDS) h5file.get(internalImage);
	            extractQuickLook();
	            List<Object> metadata = new ArrayList<Object>();
	            metadata.addAll(h5file.get("/").getMetadata());
	            metadata.addAll(h5file.get(internalImage.substring(0, 3)).getMetadata());

	            //read metadata groups
	            H5Group main=((H5Group)h5file.get("/"));
	        	List<HObject>hObjs=main.getMemberList();
	        	Iterator<HObject> it=hObjs.iterator();
	        	metadataGroup=new HashMap<String,List<Object>>();
	        	
	        	//put metadata for each group in the metadataGroup map
	        	while(it.hasNext()){
	        		HObject obj=it.next();
	        		if(obj.getName().startsWith("S"))
	        			metadataGroup.put(obj.getName(), obj.getMetadata());
	        	}
	            
	            if(imagedata==null)
	            	return false;
	        	metadata.addAll(imagedata.getMetadata());
	        	
	            long[] selected = imagedata.getSelectedDims(); // the selected size of the dataset
	            selected[0]=1;
	            if(selected.length>2)
	            	selected[2]=2;

	            //read image dimensions
	            xSize = (int) imagedata.getDims()[1];
	            ySize = (int) imagedata.getDims()[0];

	            //
	            stride = imagedata.getStride();
	            //
	            dims = imagedata.getSelectedDims();
	            //
	            starts = imagedata.getStartDims();
	            
	            setMetaWidth(xSize);
	            setMetaHeight(ySize);
	            
	            bounds = new Rectangle(0, 0, xSize, ySize);
	            gcps = new ArrayList<Gcp>();
	            
	            for (Object o : metadata) {
	                Attribute a = (Attribute) o;
	                if (a.getName().equals("Bottom Left Geodetic Coordinates")) {
	                    double[] val = (double[]) a.getValue();
	                    Gcp gcp = new Gcp();
	                    gcp.setXpix(0);
	                    gcp.setYpix(ySize);
	                    gcp.setOriginalXpix(0.0);
	                    gcp.setXgeo(val[1]);
	                    gcp.setYgeo(val[0]);
	                    gcps.add(gcp);
	                } else if (a.getName().equals("Bottom Right Geodetic Coordinates")) {
	                    double[] val = (double[]) a.getValue();
	                    Gcp gcp = new Gcp();
	                    gcp.setXpix(xSize);
	                    gcp.setOriginalXpix(new Double(xSize));
	                    gcp.setYpix(ySize);
	                    gcp.setXgeo(val[1]);
	                    gcp.setYgeo(val[0]);
	                    gcps.add(gcp);
	                } else if (a.getName().equals("Top Left Geodetic Coordinates")) {
	                    double[] val = (double[]) a.getValue();
	                    Gcp gcp = new Gcp();
	                    gcp.setXpix(0);
	                    gcp.setOriginalXpix(new Double(xSize));
	                    gcp.setYpix(0);
	                    gcp.setXgeo(val[1]);
	                    gcp.setYgeo(val[0]);
	                    gcps.add(gcp);
	                } else if (a.getName().equals("Top Right Geodetic Coordinates")) {
	                    double[] val = (double[]) a.getValue();
	                    Gcp gcp = new Gcp();
	                    gcp.setXpix(xSize);
	                    gcp.setOriginalXpix(new Double(xSize));
	                    gcp.setYpix(0);
	                    gcp.setXgeo(val[1]);
	                    gcp.setYgeo(val[0]);
	                    gcps.add(gcp);
	                } else if (a.getName().equals("Scene Sensing Start UTC")) {
	                    String[] val = (String[]) a.getValue();
	                    setTimeStampStart(val[0]);
	                } else if (a.getName().equals("Scene Sensing Stop UTC")) {
	                    String[] val = (String[]) a.getValue();
	                    setTimeStampStop(val[0]);
	                } else if (a.getName().equals("Equivalent Number of Looks")) {
	                    double[] val = (double[]) a.getValue();
	                    setENL(String.valueOf(val[0]));
	                } else if (a.getName().equals("Column Spacing")) {
	                    double[] val = (double[]) a.getValue();
	                    setRangeSpacing(new Float(val[0]));
	                    pixelsize[0]=getRangeSpacing();
	                } else if (a.getName().equals("Far Incidence Angle")) {
	                    double[] val = (double[]) a.getValue();
	                    setIncidenceFar(new Float(val[0]));
	                } else if (a.getName().equals("Near Incidence Angle")) {
	                    double[] val = (double[]) a.getValue();
	                    setIncidenceNear(new Float(val[0]));
	                } else if (a.getName().equals("Line Spacing")) {
	                    double[] val = (double[]) a.getValue();
	                    setAzimuthSpacing(new Float(val[0]));
	                    pixelsize[1]=getAzimuthSpacing();
	                } else if (a.getName().equals("Look Side")) {
	                    String[] val = (String[]) a.getValue();
	                    setLookDirection(val[0]);
	                } else if (a.getName().equals("Orbit Direction")) {
	                    String[] val = (String[]) a.getValue();
	                    setOrbitDirection(val[0]);
	                } else if (a.getName().equals("Processing Centre")) {
	                    String[] val = (String[]) a.getValue();
	                    setProcessor(val[0]);
	                } else if (a.getName().equals("Radar Wavelength")) {
	                    double[] val = (double[]) a.getValue();
	                    setRadarWaveLenght(val[0]);
	                } else if (a.getName().equals("Product Type")) {
	                    String[] val = (String[]) a.getValue();
	                    setType(val[0]);
	                } else if (a.getName().equals("Satellite ID")) {
	                    String[] val = (String[]) a.getValue();
	                    setSatellite(val[0]);
	                } else if (a.getName().equals("Satellite Height")) {
	                    double[] val = (double[]) a.getValue();
	                    setSatelliteAltitude(val[0]);
	                } else if (a.getName().equals("Multi-Beam ID")) {
	                    String[] val = (String[]) a.getValue();
	                    setBeam(val[0]);
	                }else if (a.getName().equals("PRF")) {
	                	double[] val = (double[]) a.getValue();
	                    setPRF(val[0]);
	                }else if (a.getName().equals("")) {
	                	double[] val = (double[]) a.getValue();
	                    setPRF(val[0]);
	                }
	            }
	            if (getType().startsWith("SCS")) {
	                complex = true;
	            }
                
                //set Polarization and prf
            	Collection aGroup=metadataGroup.values();
            	Iterator ig=aGroup.iterator();
            	double prf=0;
            	int i=0;
            	int idPol=24;
            	int idPrf=23;
            	if(complex){
            		idPol++;
            		idPrf++;
            	}
            	while(ig.hasNext()){
            		List<Attribute>atts=(List)ig.next();
            		if(i==0){
            			String[] val = (String[]) atts.get(idPol).getValue();
            			setPolarization(val[0]);
            		}	
            		Object prfObj=atts.get(idPrf).getValue();
            		if(prfObj instanceof double[]){
            			double[] v=(double[])prfObj; 
            			prf=prf+v[0];
            		}else{
            			String[] v=(String[])prfObj; 
            			prf=prf+Double.parseDouble(v[0]);
            		}	
                    i++;
            	}
            	prf=prf/i;
            	setPRF(prf);
                
                setSatelliteOrbitInclination(97.86);
                setRevolutionsPerday(14.8125);
                
	            setSensor("CS");
	            
	            geotransform = GeoTransformFactory.createFromGcps(gcps, "EPSG:4326");

	        } catch (Exception ex) {
	        	logger.error(null, ex);
	            return false;
	        }
	        return true;
	    }
}