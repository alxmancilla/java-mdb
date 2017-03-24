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



public class ExtractMetadataFromFileNames {

  public static void main(String[] args) throws IOException {
  
    MongoClient mongo = new MongoClient("localhost", 27017);
  /*
  MongoClient mongo = new MongoClient(new MongoClientURI("mongodb://demo:demo00@mancilla.local:27000,mancilla.local:27001,mancilla.local:27002/?authSource=admin&replicaSet=localRS&connectTimeoutMS=30000&maxPoolSize=10&w=majority&readPreference=secondaryPreferred"));

    MongoClient mongo = new MongoClient(new MongoClientURI("mongodb://mancilla.local:27000,mancilla.local:27001,mancilla.local:27002/?replicaSet=rsDemo&maxPoolSize=10&w=majority&readPreference=secondaryPreferred"));

*/

  DB db = mongo.getDB("test");

    //Location of file to be saved
    String imageLocation = "/Users/mancilla/Desktop/sourceFiles/52318ABCD123456DFHTEK99201220170224043.jpg";

    //Create instance of GridFS implementation
    GridFS gridFs = new GridFS(db);

    //Create a file entry for the image file
    File fileHandle = new File(imageLocation);
    GridFSInputFile gridFsInputFile = gridFs.createFile(fileHandle);

    //Set a name on GridFS entry
    gridFsInputFile.setFilename(fileHandle.getName());
    gridFsInputFile.setContentType("image/jpg");
    DBObject obj = new BasicDBObject();
    
    obj.put( "afore", new java.util.Date() );
    obj.put( "proceso", new Integer(1));
    obj.put( "curp" , new Integer(1));

    gridFsInputFile.setMetaData(obj);

    //Save the file to MongoDB
    gridFsInputFile.save();
    mongo.close();
  }
}



public class ImageFilenamePensionISSSTE {

  /**
    5XX18AAAA999999NNNNNN99TiiCAAAAMMDD###.ZZZ

    5XX - Clave de la Administradora Receptora según Catálogo de Entidades
    18 - Proceso al que corresponde la imagen
    AAAA999999NNNNNN99 -  CURP del trabajador
    T - tipo de trabajador (1 - IMSS, 2- ISSSTE)
    ii - Clave de la imagen (según catalogo)
    C - Consecutivo del documento imagen, del 1 al 9, al menos 1 por cada clave de imagen
    AAAAMMDD - Fecha de proceso de la solicitud (ultimo dia habil de la semana)
    ### - folio de la imagen del trabajador (llenar con 0 a la derecha, si es que se tienen 2 dígitos)
    ZZZ - extension/formato que puede ser: tiff, jpg, jpeg, o pdf

  */

    private String fullPath;
    private char pathSeparator, 
                 extensionSeparator;

    public Filename(String str, char sep, char ext) {
        fullPath = str;
        pathSeparator = sep;
        extensionSeparator = ext;
    }

    public String extension() {
        int dot = fullPath.lastIndexOf(extensionSeparator);
        return fullPath.substring(dot + 1);
    }

    // gets filename without extension
    public String filename() {
        int dot = fullPath.lastIndexOf(extensionSeparator);
        int sep = fullPath.lastIndexOf(pathSeparator);
        return fullPath.substring(sep + 1, dot);
    }

    public String path() {
        int sep = fullPath.lastIndexOf(pathSeparator);
        return fullPath.substring(0, sep);
    }

    public Integer afore() {
       //5XX - Clave de la Administradora Receptora según Catálogo de Entidades
       return new Integer(fullPath.subtring(0, 3));
    }

    public Integer proceso() {
      // 18 - Proceso al que corresponde la imagen
      return new Integer(fullPath.substring(2, 2);
    }




}


