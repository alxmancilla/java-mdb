import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
 
import org.bson.types.Binary;
 
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
  
/**
 * Java MongoDB : Save image example
 * 
 */
  
public class SaveFileonDocument {
 
    public static void main(String[] args) 
    {
        SaveFileonDocument o = new SaveFileonDocument();
                 
        /** Stores image in a collection **/
        o.withoutUsingGridFS();
    }
 
    void withoutUsingGridFS() 
    {
        Mongo mongo = null;
        DB db;
        DBCollection collection;
        try {
  
            mongo = new Mongo("localhost", 27017);
            db = mongo.getDB("test");
            collection = db.getCollection("docimages");
             
            String filename = "/Users/mancilla/Desktop/sourceFiles/foto_1.jpg";
            String empname ="Nombre del Empleado";
             
            /** Inserts a record with name = empname and photo 
              *  specified by the filepath 
              **/
            insert(empname,filename,collection);
             
            String destfilename = "/Users/mancilla/Desktop/targetFiles/foto_target.jpg";
            /** Retrieves record where name = empname, including his photo. 
              * Retrieved photo is stored at location filename 
              **/
            retrieve(empname, destfilename, collection);
             
        } catch (MongoException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
          if(mongo != null)
            mongo.close();
        }
    }
     
    void insert(String empname, String filename, DBCollection collection)
    {
        try
        {
            File imageFile = new File(filename);
            FileInputStream f = new FileInputStream(imageFile);
 
            byte b[] = new byte[f.available()];
            f.read(b);
 
            Binary data = new Binary(b);
            BasicDBObject o = new BasicDBObject();
            o.append("name",empname).append("photo",data);
            collection.insert(o);
            System.out.println("Inserted record.");
 
            f.close();
 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     
    void retrieve(String name, String filename, DBCollection collection)
    {
        byte c[];
        try
        {
            DBObject obj = collection.findOne(new BasicDBObject("name", name));
            String n = (String)obj.get("name");
            c = (byte[])obj.get("photo");
            FileOutputStream fout = new FileOutputStream(filename);
            fout.write(c);
            fout.flush();
            System.out.println("Photo of "+name+" retrieved and stored at "+filename);
            fout.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

