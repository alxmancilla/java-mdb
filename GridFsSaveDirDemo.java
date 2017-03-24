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

public class GridFsSaveDirDemo {

  public static void main(String[] args) throws IOException {
    
    MongoClient mongo = new MongoClient("mancilla.local", 27017);
  
  /*
  MongoClient mongo = new MongoClient(new MongoClientURI("mongodb://demo:demo00@mancilla.local:27000,mancilla.local:27001,mancilla.local:27002/?authSource=admin&replicaSet=localRS&connectTimeoutMS=30000&maxPoolSize=10&w=majority&readPreference=secondaryPreferred"));
    MongoClient mongo = new MongoClient(new MongoClientURI("mongodb://mancilla.local:27000,mancilla.local:27001,mancilla.local:27002/?replicaSet=rsDemo&maxPoolSize=10&w=majority&readPreference=secondaryPreferred"));
    */

    DB db = mongo.getDB("digital");

    //Location of file to be saved
    String dirLocation = "/Users/mancilla/Desktop/sourceFiles";

    //Create instance of GridFS implementation
    GridFS gridFs = new GridFS(db);

    File folder = new File(dirLocation);
    File[] listOfFiles = folder.listFiles();

    for (int i = 0; i < listOfFiles.length; i++) {
      if (listOfFiles[i].isFile()) {
        System.out.println("File " + listOfFiles[i].getName());
        //Create a file entry for the image file
        GridFSInputFile gridFsInputFile = gridFs.createFile(listOfFiles[i]);

        String filename = listOfFiles[i].getName().toLowerCase();
        String contentType = "image/jpeg";
        //Set a name on GridFS entry
        gridFsInputFile.setFilename(filename);
        if(filename.endsWith("mp3") ){
            contentType = "video/mpeg";
        }else if(filename.endsWith("mp4")) {
          contentType ="video/mp4";
        }else if(filename.endsWith("docx") || filename.endsWith("doc") ) {
          contentType ="application/msword";
        }else if(filename.endsWith("pdf")) {
          contentType ="application/pdf";
        }

        gridFsInputFile.setContentType("image/jpg");
        DBObject obj = new BasicDBObject();
        obj.put( "fecha", new java.util.Date() );
        obj.put( "version", new Integer(1));
        obj.put( "userID" , new Integer(i));

        gridFsInputFile.setMetaData(obj);

        //Save the file to MongoDB
        gridFsInputFile.save();

      } else if (listOfFiles[i].isDirectory()) {
        System.out.println("Directory " + listOfFiles[i].getName());
      }
    }

    mongo.close();

  }
}

