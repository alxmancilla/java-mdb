import java.io.File;
import java.io.IOException;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;



public class GridFsSaveDemo {

  public static void main(String[] args) throws IOException {
  /*
    MongoClient mongo = new MongoClient("localhost", 27017);
    MongoClient mongo = new MongoClient(new MongoClientURI("mongodb://demo:demo00@mancilla.local:27000,mancilla.local:27001,mancilla.local:27002/?authSource=admin&replicaSet=localRS&connectTimeoutMS=30000&maxPoolSize=10&w=majority&readPreference=secondaryPreferred"));
  */

    MongoClient mongo = new MongoClient(new MongoClientURI("mongodb://mancilla.local:27000,mancilla.local:27001,mancilla.local:27002/?replicaSet=rsDemo&maxPoolSize=10&w=majority&readPreference=secondaryPreferred"));

  DB db = mongo.getDB("digital");

    //Location of file to be saved
    String imageLocation = "/Users/mancilla/Desktop/sourceFiles/foto_1.jpg";

    //Create instance of GridFS implementation
    GridFS gridFs = new GridFS(db);

    //Create a file entry for the image file
    File fileHandle = new File(imageLocation);
    GridFSInputFile gridFsInputFile = gridFs.createFile(fileHandle);

    //Set a name on GridFS entry
    gridFsInputFile.setFilename(fileHandle.getName());
    gridFsInputFile.setContentType("image/jpg");
    DBObject obj = new BasicDBObject();
    obj.put( "fecha", new java.util.Date() );
    obj.put( "version", new Integer(1));
    obj.put( "userID" , new Integer(1));

    gridFsInputFile.setMetaData(obj);

    //Save the file to MongoDB
    gridFsInputFile.save();
    mongo.close();
  }
}

