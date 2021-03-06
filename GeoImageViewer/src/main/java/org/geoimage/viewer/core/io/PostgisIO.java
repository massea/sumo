/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geoimage.viewer.core.io;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.geoimage.def.GeoImageReader;
import org.geoimage.def.GeoTransform;
import org.geoimage.def.SarImageReader;
import org.geoimage.viewer.core.GeometryImage;
import org.geoimage.viewer.core.layers.visualization.AttributesGeometry;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureStore;
//import org.geotools.data.postgis.PostgisDataStore;
//import org.geotools.data.postgis.PostgisDataStoreFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;

/**
 *
 * @author thoorfr
 */
public class PostgisIO extends AbstractVectorIO {
	private static org.slf4j.Logger logger=LoggerFactory.getLogger(PostgisIO.class);

    public static String CONFIG_DBTYPE = "dbtype";    //must be postgis
    public static String CONFIG_HOST = "host";        //the name or ip address of the machine running PostGIS
    public static String CONFIG_PORT = "port";        //the port that PostGIS is running on (generally 5432)
    public static String CONFIG_DATABASE = "database";//the name of the database to connect to.
    public static String CONFIG_USER = "user";        //the user to connect with
    public static String CONFIG_PASSWORD = "passwd";  //the password of the user

    private GeoImageReader gir=null;
    private Map<String,String> config=null;
    private GeometryImage glayer=null;
    
    public PostgisIO(GeoImageReader gir,Map config){
    	this.gir=gir;
    	this.config=config;
    }
    public GeometryImage read() {
    	glayer=readLayer();
    	return glayer;
    }
    
    public GeometryImage readLayer() {
        try {
            GeometryImage out = null;
            DataStore dataStore = DataStoreFinder.getDataStore(config);
            FeatureSource featureSource = dataStore.getFeatureSource(layername);
            String geomName = featureSource.getSchema().getGeometryDescriptor().getLocalName();
            GeoTransform gt = gir.getGeoTransform();
            FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
            double[] x0;
            double[] x1;
            double[] x2;
            double[] x3;
            x0 = gt.getGeoFromPixel(0, 0);
            x1 = gt.getGeoFromPixel(gir.getWidth(), gir.getHeight());
            x2 = gt.getGeoFromPixel(gir.getWidth(), 0);
            x3 = gt.getGeoFromPixel(0, gir.getHeight());
            double minx = x0[0];
            double maxx = Math.max(x0[0], Math.max(x1[0], Math.max(x2[0], x3[0])));
            double miny = Math.min(x0[1], Math.min(x1[1], Math.min(x2[1], x3[1])));
            double maxy = Math.max(x0[1], Math.max(x1[1], Math.max(x2[1], x3[1])));
            ReferencedEnvelope bbox = new ReferencedEnvelope(minx, maxx, miny, maxy, CRS.decode("EPSG:4326"));
            Polygon imageP = (Polygon) new WKTReader().read("POLYGON((" +
                    x0[0] + " " + x0[1] + "," +
                    x1[0] + " " + x1[1] + "," +
                    x2[0] + " " + x2[1] + "," +
                    x3[0] + " " + x3[1] + "," +
                    x0[0] + " " + x0[1] + "" +
                    "))");
            Filter filter = ff.bbox(ff.property(geomName), bbox);
            FeatureCollection fc = featureSource.getFeatures(filter);
            if (fc.isEmpty()) {
                return null;
            }

            String[] schema = createSchema(fc.getSchema().getDescriptors());
            String[] types = createTypes(fc.getSchema().getDescriptors());
            String geoName = fc.getSchema().getGeometryDescriptor().getType().getName().toString();
            if (geoName.contains("Polygon")) {
                out = new GeometryImage(GeometryImage.POLYGON);
                out.setName(layername);
                FeatureIterator fi = fc.features();
                try{
	                while (fi.hasNext()) {
	                    Feature f = fi.next();
	                    Geometry p2 = ((Geometry)f.getDefaultGeometryProperty().getValue()).intersection(imageP);
	                    AttributesGeometry at = new AttributesGeometry(schema);
	                    for (int i = 0; i < f.getProperties().size(); i++) {
	                        at.set(schema[i], f.getProperty(schema[i]).getValue());
	                    }
	                    out.put(p2, at);
	                }
                }finally{    
	            	fi.close();
	            }	
            } else if (geoName.contains("Point")) {
                out = new GeometryImage(GeometryImage.POINT);
                out.setName(layername);
                FeatureIterator fi = fc.features();
                try{
	                while (fi.hasNext()) {
	                    Feature f = fi.next();
	                    Geometry p2 = ((Geometry)f.getDefaultGeometryProperty().getValue());
	                    AttributesGeometry at = new AttributesGeometry(schema);
	                    for (int i = 0; i < f.getProperties().size(); i++) {
	                        at.set(schema[i], f.getProperty(schema[i]).getValue());
	                    }
	                    out.put(p2, at);
	                }
	                fi.close();
	            }finally{    
	            	fi.close();
	            }
            }
            dataStore.dispose();
            return GeometryImage.createImageProjected(out, gt, "EPSG:4326");
        } catch (Exception ex) {
        	logger.error(ex.getMessage(),ex);
        }
        return null;
    }

    public List<? extends Object> executeCommands(List<String> commands) {
        List<Integer> out = new ArrayList<Integer>();
        Connection conn;
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://" + config.get("host") + ":" + config.get("port") + "/" + config.get("dbname");

            conn = DriverManager.getConnection(url, (String) config.get("user"), (String) config.get("password"));
            Statement s = conn.createStatement();
            try {
                for (String postgiscommand : commands) {
                    System.out.println(postgiscommand);
                    out.add(s.executeUpdate(postgiscommand));
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            } finally {
                s.close();
                conn.close();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Could not save data to database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return out;
    }





/*

    private static PostgisDataStore createDataStore(Map config) throws Exception {
        PostgisDataStoreFactory factory = new PostgisDataStoreFactory();
        PostgisDataStore myData = (PostgisDataStore) factory.createDataStore(config);

        return myData;
    }

*/








    public FeatureCollection createFeatures(SimpleFeatureType ft, GeometryImage glayer, String projection, GeoTransform gt) throws Exception {
    	 //   FeatureCollection collection =  FeatureCollections.newCollection();
        DefaultFeatureCollection collection = new DefaultFeatureCollection();
        SimpleFeature simplefeature = null;
        GeometryFactory gf = new GeometryFactory();

        for (Geometry geom : glayer.getGeometries()) {
            if (geom instanceof Point) {
                Object[] data = new Object[ft.getDescriptors().size()];
                //System.arraycopy(glayer.getAttributes(geom).getValues(), 0, data, 0, data.length);
                //data[2] = geom;
                //SimpleFeature simplefeature = SimpleFeatureBuilder.build(ft, data, "Point");
                //collection.add(simplefeature);
                System.arraycopy(glayer.getAttributes(geom).getValues(), 0, data, 0, data.length-1);
                // geocodification of boats
                double[] pos = gt.getGeoFromPixel(geom.getCoordinate().x, geom.getCoordinate().y);
                // update of x,y pixel coordinates to geocoded lon,lat
                Geometry geomgr = gf.createPoint(new Coordinate(pos[0], pos[1]));
                data[data.length-1] = geomgr;
                simplefeature = SimpleFeatureBuilder.build(ft, data, null);
                collection.add(simplefeature);

            } else if (geom instanceof Polygon) {
                Object[] data = new Object[glayer.getSchema().length + 1];
                data[0] = geom;
                System.arraycopy(glayer.getAttributes(geom).getValues(), 0, data, 1, data.length - 1);
                simplefeature = SimpleFeatureBuilder.build(ft, data, "Polygon");
                collection.add(simplefeature);
            }
        }
        return collection;
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

    public static FeatureCollection createTabFeatures(SimpleFeatureType ft, Object[] values) throws Exception {
    	 DefaultFeatureCollection collection = new DefaultFeatureCollection();
         Object[] data = new Object[ft.getDescriptors().size()];
         System.arraycopy(values, 1, data, 0, data.length);
         SimpleFeature simplefeature = SimpleFeatureBuilder.build(ft, data, null);
         collection.add(simplefeature);
         return collection;
    }
	/* TODO:check if we need this function
     *
     * @param name
     * @param glayer
     * @return
     *
    public static SimpleFeatureType createFeatureType(String name, GeometricLayer glayer) {
        try {

            // Tell this shapefile what type of data it will store
            // Shapefile handle only : Point, MultiPoint, MultiLineString, MultiPolygon
            String sch = "";
            String[] schema = glayer.getSchema();
           // String[] types = glayer.getSchemaTypes();
            for (int i = 0; i < schema.length; i++) {
                sch += "," + schema[i] + ":" + types[i];
            }
            String geomType = "Polygon";
            if (glayer.getGeometries().get(0) instanceof Point) {
                geomType = "Point";
            }
            SimpleFeatureType featureType = DataUtilities.createType(name, "geom:" + geomType + sch);

            //to create other fields you can use a string like :
            // "geom:MultiLineString,FieldName:java.lang.Integer"
            // field name can not be over 10 characters
            // use a ',' between each field
            // field types can be : java.lang.Integer, java.lang.Long,
            // java.lang.Double, java.lang.String or java.util.Date

            return featureType;


        } catch (SchemaException ex) {
        	logger.error(ex.getMessage(),ex);
        }
        return null;
    }
   /**
    *
    * @param attributeTypes
    * @return
    */

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
            out[i++] = at.getType().getName().toString();
        }
        return out;
    }

    private static void writeToDB(DataStore data, FeatureCollection collection) {
        FeatureStore store = null;
        DefaultTransaction transaction = new DefaultTransaction();
        try {
            String featureName = data.getTypeNames()[0];
            // Tell it the name of the shapefile it should look for in our DataStore
            store = (FeatureStore) data.getFeatureSource(featureName);
            // Then set the transaction for that FeatureStore
            store.setTransaction(transaction);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            store.addFeatures(collection);
            transaction.commit();
            transaction.close();
        } catch (Exception ex) {
            try {
                transaction.rollback();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    
	/**
	 * Save the corresponding VDS into corresponding format [kmz,xml,ddbb]
	 * @param layer
	 * @param projection
	 */
    @Override
    public void save(File output, String projection,GeoTransform transform) {
        try {
        	//Data store to get access to ddbb
            DataStore datastore = (DataStore) DataStoreFinder.getDataStore(config);
            //Access to the corresponding VDS layer on ddbb f.i: 'VESSELS'
            FeatureStore featurestore = (FeatureStore) datastore.getFeatureSource(layername);
            SimpleFeatureType featuretype = (SimpleFeatureType) featurestore.getSchema();
            /* @FIX I need explanation
             impleFeatureTypeBuilder builder =  new SimpleFeatureTypeBuilder();
            builder.setName(featuretype.getName());

            builder.setCRS(featuretype.getCoordinateReferenceSystem());
            System.out.println(featuretype.getDescriptors().size());
            //build the type
            //SimpleFeatureType type = builder.buildFeatureType();
            // System.out.println(type.getDescriptors().size());
            //*/
            FeatureCollection features = createFeatures(featuretype, glayer, projection, gir.getGeoTransform());
            featurestore.addFeatures(features);
            writeToDB(datastore, features);
            datastore.dispose();


        } catch (Exception ex) {
        	logger.error(ex.getMessage(),ex);
        }
    }

    /**
     * Method to store analysed image params in a table on the DDBB for posterior data mining
     *
     * @param layer
     * @param projection
     * @param imageCis
     */
    public void saveAll(GeometryImage layer, String projection, Object[] imageCis,GeoImageReader gir) {
        try {
        	//Data store to get access to ddbb
            DataStore datastore = (DataStore) DataStoreFinder.getDataStore(config);
            //Access to the corresponding Imagery table on ddbb
            FeatureStore featurestore = (FeatureStore) datastore.getFeatureSource(imageCis[0].toString());
            SimpleFeatureType featuretype = (SimpleFeatureType) featurestore.getSchema();
            FeatureCollection features = createTabFeatures(featuretype,imageCis);
            featurestore.addFeatures(features);
            writeToDB(datastore, features);
            datastore.dispose();
            // store extracted VDS points
            save(null,projection,((SarImageReader)gir).getGeoTransform());
        } catch (Exception ex) {
        	logger.error(ex.getMessage(),ex);
        }
    }
	




}
