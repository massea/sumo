/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geoimage.viewer.core.io;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.geoimage.def.GeoTransform;
import org.geoimage.viewer.core.GeometryImage;
import org.geoimage.viewer.core.SumoPlatform;
import org.geoimage.viewer.core.gui.manager.LayerManager;
import org.geoimage.viewer.core.layers.visualization.AttributesGeometry;
import org.geoimage.viewer.util.JTSUtil;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureWriter;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFactorySpi;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.collection.CollectionFeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.process.vector.ClipProcess;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.referencing.operation.MathTransform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;

/**
 *
 * @author thoorfr
 */
public class SimpleShapefile extends AbstractVectorIO{
	private static Logger logger= LoggerFactory.getLogger(SimpleShapefile.class);
    public static String CONFIG_URL = "url";


    private File shpInput=null;
    private GeoTransform transform=null;
    private GeometryImage layer=null;


    public SimpleShapefile(File input,GeoTransform transform) {
    	this.shpInput=input;
    	this.transform=transform;
    }

    @SuppressWarnings("unused")
	private static FileDataStore createDataStore(String filename, SimpleFeatureType ft, String projection) throws Exception {
       	File file = new File(filename);
        FileDataStore dataStore = FileDataStoreFinder.getDataStore(file);
     // Tell the DataStore what type of Coordinate Reference System (CRS) to use
        if (projection != null) {
            ((ShapefileDataStore)dataStore).forceSchemaCRS(CRS.decode(projection));
        }

        return dataStore;
    }



	@Override
	public GeometryImage read() {
		this.layer=createLayer(this.shpInput,transform);
		return layer;
	}

	/**
	 *
	 * @param shpInput
	 * @param transform
	 * @return
	 */
	public static GeometryImage createLayer(File shpInput,GeoTransform transform) {
    	GeometryImage out=null;
        try {
        	Map<String, Serializable> params = new HashMap<String, Serializable>();
            params.put("url", shpInput.toURI().toURL());

            //create a DataStore object to connect to the physical source
            DataStore dataStore = DataStoreFinder.getDataStore(params);
            //retrieve a FeatureSource to work with the feature data
            SimpleFeatureSource featureSource = (SimpleFeatureSource) dataStore.getFeatureSource(dataStore.getTypeNames()[0]);


            SimpleFeatureCollection fc=featureSource.getFeatures();

            if (fc.isEmpty()) {
                return null;
            }
            String[] schema = createSchema(fc.getSchema().getDescriptors());
            String[] types = createTypes(fc.getSchema().getDescriptors());

            String geoName = fc.getSchema().getGeometryDescriptor().getType().getName().toString();


            out=GeometryImage.createLayerFromFeatures(geoName, dataStore,fc , schema, types,true ,transform);
            dataStore.dispose();

            //glout = GeometricLayer.createImageProjectedLayer(out, transform,null);


        } catch (Exception ex) {
        	logger.error(ex.getMessage(),ex);
        }
        return out;

    }

	/**
	 *
	 * @param shpInput
	 * @param bbox
	 * @param transform
	 * @return
	 */
    public static GeometryImage createIntersectedLayer(File shpInput,Polygon bbox,GeoTransform transform) {
    	GeometryImage glout=null;
    	DataStore dataStore =null;
        try {
        	if(shpInput!=null){
	        	Map<String, Serializable> params = new HashMap<String, Serializable>();
	            params.put("url", shpInput.toURI().toURL());

	            //create a DataStore object to connect to the physical source
	            dataStore = DataStoreFinder.getDataStore(params);
	            //retrieve a FeatureSource to work with the feature data
	            SimpleFeatureSource featureSource = (SimpleFeatureSource) dataStore.getFeatureSource(dataStore.getTypeNames()[0]);

	            Polygon imageP=bbox;

	            ClipProcess clip=new ClipProcess();
	            SimpleFeatureCollection fc=clip.execute(featureSource.getFeatures(), imageP,true);
	            //exportFeaturesToShapeFile(new File("H:\\tmp\\test.shp"), fc);
	            if (fc.isEmpty()) {
	                return null;
	            }
	            String[] schemaStr = createSchema(fc.getSchema().getDescriptors());
	            String[] types = createTypes(fc.getSchema().getDescriptors());

	            String geoName = fc.getSchema().getGeometryDescriptor().getType().getName().toString();

	            boolean applayT=false;
	            if(transform!=null)
	            	applayT=true;
	            glout=GeometryImage.createFromSimpleGeometry(imageP, geoName, fc, schemaStr, types,applayT,transform);
	            glout.setName(shpInput.getName());
	            glout.splitMultiPolygons();
	            /*exportGeometriesToShapeFile(glout.getGeometries(), 
	            		new File("E:\\tmp\\test2.shp"), glout.getGeometryType(), 
	            		nulltransform, featureSource.getSchema());*/

        	}
        } catch (Exception ex) {
        	logger.error(ex.getMessage(),ex);
        }finally{
        	if(dataStore!=null)
        		dataStore.dispose();
        }
        return glout;

    }

    public static void cleanAndSaveShapeFile(File shpInput,File shpOutput){
    	DataStore dataStore =null;
        try {
        	if(shpInput!=null){

	            //create a DataStore object to connect to the physical source
	            dataStore = FileDataStoreFinder.getDataStore(shpInput);
	            //retrieve a FeatureSource to work with the feature data
	            SimpleFeatureSource featureSource = (SimpleFeatureSource) dataStore.getFeatureSource(dataStore.getTypeNames()[0]);

	            SimpleFeatureCollection corrected=correctFeatures(featureSource.getFeatures());


	            exportFeaturesToShapeFile(shpOutput, featureSource.getFeatures());


        	}
        }catch(Exception e){

        }
    }




    public static SimpleFeatureCollection correctFeatures(SimpleFeatureCollection fc){
    	SimpleFeatureIterator iterator=fc.features();
    	DefaultFeatureCollection outVector = new DefaultFeatureCollection();

    	while(iterator.hasNext()){
    		SimpleFeature sf=iterator.next();
    		Geometry gm=(Geometry)sf.getDefaultGeometry();
    		if(!gm.isValid()){
    			gm=JTSUtil.repair(gm);
    			System.out.println(gm.isValid());
    		}
			sf.setDefaultGeometry(gm);
		    outVector.add(sf);

    	}

    	return fc;
    }


    /**
     *
     * @param layer
     * @param shpInput
     * @param transform
     * @return
     * @throws Exception
     */
    public static GeometryImage mergeShapeFile(SimpleFeatureCollection collectionsLayer, File shpInput,GeoTransform transform,Polygon bbox)throws Exception {
    	Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("url", shpInput.toURI().toURL());

        //create a DataStore object to connect to the physical source
        DataStore dataStore = DataStoreFinder.getDataStore(params);

        SimpleFeatureSource shape2 = (SimpleFeatureSource) dataStore.getFeatureSource(dataStore.getTypeNames()[0]);

        SimpleFeatureCollection collectionsShape2=shape2.getFeatures();
        SimpleFeatureType schemaShape2=shape2.getSchema();

        ClipProcess clip=new ClipProcess();
        SimpleFeatureCollection fc=clip.execute(collectionsShape2, bbox,true);

        SimpleFeatureType schemaLayer=collectionsLayer.getSchema();

        //merge the schema and the types
        SimpleFeatureTypeBuilder stb = new SimpleFeatureTypeBuilder();
        stb.setName("merged_geom");
        stb.setCRS(schemaLayer.getCoordinateReferenceSystem());
        stb.addAll(schemaShape2.getAttributeDescriptors());
        stb.addAll(schemaLayer.getAttributeDescriptors());
        SimpleFeatureType newFeatureType = stb.buildFeatureType();

        //create new datastore to save the new shapefile
        FileDataStoreFactorySpi factory = new ShapefileDataStoreFactory();
        File tmp=new File(SumoPlatform.getApplication().getCachePath()+"\\tmpshape_"+System.currentTimeMillis()+".shp");
        Map<String, Serializable> params2 = new HashMap<String, Serializable>();
        params2.put("url", tmp.toURI().toURL());
        ShapefileDataStore newds=(ShapefileDataStore)factory.createNewDataStore(params2);
        GeometryImage out=null;
        try{
	        newds.createSchema(newFeatureType);

	        //merge the feaures
	        SimpleFeatureStore mergeFeat=(SimpleFeatureStore)newds.getFeatureSource();
	        mergeFeat.addFeatures(collectionsLayer);
	        mergeFeat.addFeatures(fc);
	        //save the new shape file
	        exportToShapefile(newds, mergeFeat.getFeatures());//,newds.getSchema());

	        //from here create the new GeometricLayer
	        Collection<PropertyDescriptor>descriptorsMerge=new ArrayList<>();
	        descriptorsMerge.addAll(schemaShape2.getDescriptors());
	        descriptorsMerge.addAll(schemaLayer.getDescriptors());

	        String[] schema = createSchema(descriptorsMerge);
	        String[] types = createTypes(descriptorsMerge);


	        String geoName = schemaLayer.getGeometryDescriptor().getType().getName().toString();
	        out=GeometryImage.createLayerFromFeatures(geoName, newds, mergeFeat.getFeatures(), schema, types,true,transform);
	        //out = GeometricLayer.createImageProjectedLayer(out, transform,null);
	        out.setName("merge_"+shpInput.getName()+"_"+LayerManager.getIstanceManager().getCurrentImageLayer().getName());
        }finally{
        	if(newds!=null)
        		newds.dispose();
        }
        return out;
    }

    /**
     *
     * @param collectionsLayer
     * @param shpInput
     * @param transform
     * @param bbox
     * @return
     * @throws Exception
     */
    public static GeometryImage mergeShapeFile2(SimpleFeatureCollection collectionsLayer, File shpInput,GeoTransform transform,Polygon bbox)throws Exception {
    	Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("url", shpInput.toURI().toURL());

        //create a DataStore object to connect to the physical source
        DataStore dataStore = DataStoreFinder.getDataStore(params);
        //retrieve a FeatureSource to work with the feature data
        SimpleFeatureSource shape2 = (SimpleFeatureSource) dataStore.getFeatureSource(dataStore.getTypeNames()[0]);

        ClipProcess clip=new ClipProcess();
        SimpleFeatureCollection collectionsShape2=shape2.getFeatures();
        SimpleFeatureCollection fc=clip.execute(collectionsShape2, bbox,true);
        SimpleFeatureSource source = new CollectionFeatureSource(fc);

        SimpleFeatureCollection result=joinFeaures(source, shape2);

        //create new datastore to save the new shapefile
        FileDataStoreFactorySpi factory = new ShapefileDataStoreFactory();
        File tmp=new File(SumoPlatform.getApplication().getCachePath()+"\\tmpshape_"+System.currentTimeMillis()+".shp");
        Map<String, Serializable> params2 = new HashMap<String, Serializable>();
        params2.put("url", tmp.toURI().toURL());
        ShapefileDataStore newds=(ShapefileDataStore)factory.createNewDataStore(params2);

        String geoName = collectionsLayer.getSchema().getGeometryDescriptor().getType().getName().toString();
        //String nameOutput="merge_"+shpInput.getName()+"_"+LayerManager.getIstanceManager().getCurrentImageLayer().getName();

      //from here create the new GeometricLayer
        Collection<PropertyDescriptor>descriptorsMerge=new ArrayList<>();
        descriptorsMerge.addAll(shape2.getSchema().getDescriptors());
        descriptorsMerge.addAll(collectionsLayer.getSchema().getDescriptors());

        String[] schema = createSchema(descriptorsMerge);
        String[] types = createTypes(descriptorsMerge);

        GeometryImage out=GeometryImage.createLayerFromFeatures(geoName, newds, result, schema, types,true,transform);

        return out;
    }


    /**
     *
     * @param shapes
     * @param shapes2
     * @throws Exception
     */
    private static SimpleFeatureCollection joinFeaures(SimpleFeatureSource shapes, SimpleFeatureSource shapes2) throws Exception {
    	SimpleFeatureCollection join =null;

        SimpleFeatureType schema = shapes.getSchema();
        String typeName = schema.getTypeName();
        String geomName = schema.getGeometryDescriptor().getLocalName();

        SimpleFeatureType schema2 = shapes2.getSchema();
        String typeName2 = schema2.getTypeName();
        String geomName2 = schema2.getGeometryDescriptor().getLocalName();
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

        Query outerGeometry = new Query(typeName, Filter.INCLUDE, new String[] { geomName });
        SimpleFeatureCollection outerFeatures = shapes.getFeatures(outerGeometry);
        SimpleFeatureIterator iterator = outerFeatures.features();
        int max = 0;
        try {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                try {
                    Geometry geometry = (Geometry) feature.getDefaultGeometry();
                    if (!geometry.isValid()) {
                        // skip bad data
                        continue;
                    }
                    Filter innerFilter = ff.intersects(ff.property(geomName2), ff.literal(geometry));
                    Query innerQuery = new Query(typeName2, innerFilter, Query.NO_NAMES);
                    join = shapes2.getFeatures(innerQuery);
                    int size = join.size();
                    max = Math.max(max, size);
                } catch (Exception skipBadData) {
                }
            }
        } finally {
            iterator.close();
        }
        return join;
    }







    /**
     *
     * @param output
     * @param layer
     * @param projection
     * @param reader
     */
    public void save(File output, String projection,GeoTransform transform) {
    	exportLayer(output,this.layer,projection,transform);
    }



    /**
     *
     * @param output
     * @param layer
     * @param projection
     * @param reader
     */
    public static void exportLayer(File output,GeometryImage layer, String projection,GeoTransform transform) {
        try {
            String type=layer.getGeometries().get(0).getGeometryType();
            SimpleFeatureType featureType =createFeatureType(layer.getGeometries().get(0).getClass(),(AttributesGeometry)layer.getGeometries().get(0).getUserData());
            exportGeometriesToShapeFile(layer.getGeometries(), output, type, transform,featureType);
        } catch (Exception ex) {
        	logger.error(ex.getMessage(),ex);
        }finally{
        }
    }


     /**
      *
      * @param geoms
      * @param fileOutput
      * @param geomType
      * @param transform
      * @throws IOException
      * @throws SchemaException
      */
     public static void exportGeometriesToShapeFile(final List<Geometry> geoms,
    		 File fileOutput,String geomType,GeoTransform transform,
    		 SimpleFeatureType featureType) throws IOException, SchemaException{

    	 FileDataStoreFactorySpi factory = new ShapefileDataStoreFactory();
    	 //Map map = Collections.singletonMap( "url", fileOutput.toURI().toURL() );
    	 DataStore data = factory.createDataStore( fileOutput.toURI().toURL() );
    	 boolean addAttr=true;
    	 if(featureType==null){
    		 featureType=DataUtilities.createType( "the_geom", "geom:"+geomType+",name:String,age:Integer,description:String" );
    		 addAttr=false;
    	 }
    	 data.createSchema( featureType );

    	 Transaction transaction = new DefaultTransaction();
         FeatureWriter<SimpleFeatureType, SimpleFeature> writer = data.getFeatureWriterAppend(data.getTypeNames()[0], transaction);

         SimpleFeatureBuilder featureBuilder=new SimpleFeatureBuilder(featureType);
         GeometryFactory gb=new GeometryFactory();
         try {
	         int fid=0;
	         for(final Geometry g:geoms){
	        	 Geometry clone=gb.createGeometry(g);
	        	 if(transform!=null)
	        		 clone=transform.transformGeometryGeoFromPixel(clone);

	        	 featureBuilder.add("the_geom");
	        	 featureBuilder.add(clone);
	        	 SimpleFeature sf=featureBuilder.buildFeature(""+fid++);
	        	 SimpleFeature sfout=writer.next();
	        	 sfout.setAttributes( sf.getAttributes() );
	             //setting attributes geometry
            	 AttributesGeometry att=(AttributesGeometry) g.getUserData();
            	 try{
	            	 if(att!=null&&addAttr){
		            	 String sch[]=att.getSchema();
		            	 for(int i=0;i<sch.length;i++){
		            		 Object val=att.get(sch[i]);
		            		 if(val.getClass().isArray()){
		            			 Object o=ArrayUtils.toString(val);
		            			 sfout.setAttribute(sch[i], o);
		            		 }else{
		            			 sfout.setAttribute(sch[i], val);
		            		 }
		            	 }
	            	 }
            	 }catch(Exception e ){
            		 logger.warn("Error adding attributes to geometry:"+e.getMessage());
            	 }

	             sfout.setDefaultGeometry( clone);
	        	 writer.write();
	    	 }
	         transaction.commit();
	         logger.info("Export to shapefile complete:"+ fileOutput.getAbsolutePath());
         } catch (Exception problem) {
             problem.printStackTrace();
             transaction.rollback();
             logger.error("Export to shapefile failed",problem );
         } finally {
             writer.close();
             transaction.close();
             data.dispose();
         }

     }

     public static void exportFeaturesToShapeFile(File fileOutput,FeatureCollection<SimpleFeatureType,SimpleFeature> featureCollection){
    	 DataStore data =null;
    	 try {

    		 if (!fileOutput.exists()){
    			 fileOutput.createNewFile();
    			 fileOutput.setWritable(true);
    		 }
	    	 FileDataStoreFactorySpi factory = new ShapefileDataStoreFactory();
	    	 data = factory.createDataStore( fileOutput.toURI().toURL() );

	    	 data.createSchema(featureCollection.getSchema());
	    	 exportToShapefile(data,featureCollection);
		} catch (Exception e) {
			logger.error("Export to shapefile failed",e );
		}finally{
			if(data!=null)
				data.dispose();
		}
     }


     /**
      * Export features to a new shapefile using the map projection in which
      * they are currently displayed
      */
      public static void exportToShapefile(DataStore newDataStore, FeatureCollection<SimpleFeatureType,SimpleFeature> featureCollection)/*,SimpleFeatureType ftype)*/ throws Exception {
    	 // carefully open an iterator and writer to process the results
         Transaction transaction = new DefaultTransaction();
         FeatureWriter<SimpleFeatureType, SimpleFeature> writer = newDataStore.getFeatureWriter(newDataStore.getTypeNames()[0],  transaction);

         FeatureIterator<SimpleFeature> iterator = featureCollection.features();
         try {

             while( iterator.hasNext() ){
                 SimpleFeature feature = iterator.next();
                 SimpleFeature copy = writer.next();


                 //copy.setAttributes( feature.getAttributes() );
                 Geometry geometry = (Geometry) feature.getDefaultGeometry();
                 if(geometry!=null){
                 	copy.setDefaultGeometry( geometry.buffer(0));
                 	writer.write();
                 }else{
                 	logger.warn("Warning:geometry null");
                 }
             }
             transaction.commit();
             logger.info("Export to shapefile complete" );
         } catch (Exception problem) {
             problem.printStackTrace();
             transaction.rollback();
             logger.error("Export to shapefile failed",problem );
         } finally {
             writer.close();
             iterator.close();
             transaction.close();
         }
     }

      public static void exportToShapefileForceWGS84(DataStore newDataStore, FeatureCollection<SimpleFeatureType,SimpleFeature> featureCollection)/*,SimpleFeatureType ftype)*/ throws Exception {
     	 // carefully open an iterator and writer to process the results
          Transaction transaction = new DefaultTransaction();

          //CoordinateReferenceSystem crsOrig=featureCollection.getSchema().getCoordinateReferenceSystem();

          //set wgs84 coordinates ref sys
          SimpleFeatureType featureType = SimpleFeatureTypeBuilder.retype(featureCollection.getSchema(), DefaultGeographicCRS.WGS84);
          newDataStore.createSchema(featureType);

          FeatureWriter<SimpleFeatureType, SimpleFeature> writer = newDataStore.getFeatureWriter(newDataStore.getTypeNames()[0],  transaction);
          //create the transformation
          MathTransform trans=CRS.findMathTransform(CRS.parseWKT(WKTString.WKT3995_ESRI),DefaultGeographicCRS.WGS84);


          FeatureIterator<SimpleFeature> iterator = featureCollection.features();
          try {

              while( iterator.hasNext() ){
                  SimpleFeature feature = iterator.next();
                  SimpleFeature copy = writer.next();


                  Geometry geometry = (Geometry) feature.getDefaultGeometry();
                  geometry=JTS.transform(geometry,trans);


                  if(geometry!=null){
                  	copy.setDefaultGeometry( geometry.buffer(0));
                  	writer.write();
                  }else{
                  	logger.warn("Warning:geometry null");
                  }
              }
              transaction.commit();
              logger.info("Export to shapefile complete" );
          } catch (Exception problem) {
              problem.printStackTrace();
              transaction.rollback();
              logger.error("Export to shapefile failed",problem );
          } finally {
              writer.close();
              iterator.close();
              transaction.close();
          }
      }


     /**
      *
      * @param ft
      * @param glayer
      * @param projection
      * @param gt
      * @return
      * @throws Exception
      */
     public static FeatureCollection<SimpleFeatureType,SimpleFeature>  createFeatures(SimpleFeatureType ft, GeometryImage glayer, String projection) throws Exception {
     	 DefaultFeatureCollection collection = new DefaultFeatureCollection();
          int id=0;
          for (Geometry geom : glayer.getGeometries()) {
        	  Object[] data = new Object[ft.getDescriptors().size()];
              System.arraycopy(glayer.getAttributes(geom).getValues(), 0, data, 1, data.length-1);
              data[0] = geom;
        	  SimpleFeature simplefeature = SimpleFeatureBuilder.build(ft, data, ""+id);
        	  collection.add(simplefeature);
              /*if (geom instanceof Point) {
                  Object[] data = new Object[ft.getDescriptors().size()];
                  System.arraycopy(glayer.getAttributes(geom).getValues(), 0, data, 1, data.length-1);
                  data[0] = geom;
                  SimpleFeature simplefeature = SimpleFeatureBuilder.build(ft, data, ""+id);
                  collection.add(simplefeature);
              } else if (geom instanceof Polygon) {
                  Object[] data = new Object[glayer.getSchema().length];
                  System.arraycopy(glayer.getAttributes(geom).getValues(), 0, data, 0, data.length );
                  SimpleFeature simplefeature = SimpleFeatureBuilder.build(ft, data, ""+id);
                  collection.add(simplefeature);
                  data = null;
              } else if (geom instanceof LineString) {
                  Object[] data = new Object[glayer.getSchema().length + 1];
                  data[0] = geom;
                  System.arraycopy(glayer.getAttributes(geom).getValues(), 0, data, 1, data.length - 1);
                  SimpleFeature simplefeature = SimpleFeatureBuilder.build(ft, data, ""+id);
                  collection.add(simplefeature);
              }*/
              id++;
          }
         return collection;
     }

    private static String[] createSchema(Collection<PropertyDescriptor> attributeTypes) {
        String[] out = new String[attributeTypes.size()];
        int i = 0;
        for (PropertyDescriptor at : attributeTypes) {
            out[i++] = at.getName().toString();
        }
        return out;
    }

    private static String[] createTypes(Collection<PropertyDescriptor> attributeTypes) {
        String[] out = new String[attributeTypes.size()];
        int i = 0;
        for (PropertyDescriptor at : attributeTypes) {
            out[i++] = at.getType().getBinding().getName();
        }
        return out;
    }


    private static SimpleFeatureType createFeatureType(Class geomClass,AttributesGeometry attr) {

        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("Location");
        builder.setCRS(DefaultGeographicCRS.WGS84); // <- Coordinate reference system

        // add attributes in order
        builder.add(geomClass.getSimpleName(), geomClass);
        builder.length(15).add("Name", String.class); // <- 15 chars width for name field
        builder.add("Number", Integer.class);

        //aggiungo tutti gli altri attributi allo schema
        if(attr!=null){
	        String[]schema= attr.getSchema();
	        for(int i=0;i<schema.length;i++){
	        	Object val=attr.get(schema[i]);
	        	if(val!=null){
		        	if(val.getClass().isArray()){
		        		String valArray=ArrayUtils.toString(val);
		        		builder.add(schema[i],valArray.getClass());
		        	}else{
		        		builder.add(schema[i],val.getClass());
		        	}
	        	}	
	        }
        }

        // build the type
        final SimpleFeatureType ft = builder.buildFeatureType();

        return ft;
    }










    public static void main(String[] args){
    	double cc[][]=new double[][]{{200.0,200.0},{500.0,200.0},{500.0,500.0},{200.0,500.0},{200.0,200.0}};

    	try {
    		/*Polygon p=PolygonOp.createPolygon(cc);
        	List<Geometry>geoms=new ArrayList<>();
        	geoms.add(p);
			SimpleShapefile.exportGeometriesToShapeFile(geoms,new File("F:\\SumoImgs\\export\\test.shp") , "Polygon",null,null);*/


    		/*CoordinateReferenceSystem crs = CRS.decode("EPSG:3995");
    	    String wkt = crs.toWKT();
    	    System.out.println("wkt for EPSG:3995");
    	    System.out.println( wkt );*/


    		String nameOut="/home/argenpo/Desktop/script_ice/masie_ice_r00_v01_2016037_1km/masie_ice_r00_v01_2016037_1km_corrected"
    				+ System.currentTimeMillis()+"_"
    				+".shp";

    		SimpleShapefile.cleanAndSaveShapeFile(new File("/home/argenpo/Desktop/script_ice/masie_ice_r00_v01_2016037_1km/masie_ice_r00_v01_2016037_1km.shp")
    				, new File(nameOut));


		} catch (Exception e) {
			e.printStackTrace();
		}

    }


}
