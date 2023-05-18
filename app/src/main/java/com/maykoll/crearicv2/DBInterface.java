package com.maykoll.crearicv2;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBInterface {

    public static final String TAG = "DBInterface";
    public static final int VERSION = 2;
    private final Context contexto;
    private AyudaDB ayuda;
    private SQLiteDatabase bd;
    public static final String DBNAME = "BD_Practica";
    //BD



    //Creacion de las tablas:

    //Usuarios
    public static final String TablaUsuario = "CREATE TABLE Usuarios "+
            "(id INTEGER PRIMARY KEY,"+
            "email TEXT UNIQUE,"+
            "password TEXT);";

    //OCRD
    public static final String TablaInterlocutor ="CREATE TABLE OCRD"+
            "(cardcode TEXT PRIMARY KEY,"+
            "cardName TEXT,"+
            "LicTradNum TEXT,"+
            "cellular TEXT,"+
            "e_mail TEXT,"+
            "Shiptodef TEXT,"+
            "Billtodef TEXT,"+
            "id_usuario INTEGER,"+
            "FOREIGN KEY (id_usuario) REFERENCES Usuarios (id));";

    //OCPR Contactos
    public static final String TablaContactos = "CREATE TABLE OCPR"+
            "(CardCode TEXT,"+
            "CntctPrsn TEXT,"+
            "Name TEXT,"+
            "FirstName TEXT,"+
            "E_MailL TEXT,"+
            "FOREIGN KEY (CardCode) REFERENCES OCRD (cardcode));";

    //CDR1 Direcciones
    public static final String TablaDirecciones= "CREATE TABLE CRD1"+
            "(AdresType TEXT,"+
            "Address TEXT,"+
            "Address2 TEXT,"+
            "Street TEXT,"+
            "City TEXT,"+
            "State TEXT,"+
            "Country TEXT,"+
            "ZipCode TEXT,"+
            "FOREIGN KEY (Address) REFERENCES OCRD (Shiptodef),"+
            "FOREIGN KEY (Address) REFERENCES OCRD (Billtodef));";


    public DBInterface (Context con)
    {
        this.contexto = con;
        Log.w(TAG, "creando ayuda" );
        ayuda = new AyudaDB(contexto);
    }

    //abrir BD
    public DBInterface abre () throws SQLException {
        Log.w(TAG, "abrimos base de datos" );
        bd = ayuda.getWritableDatabase();
        return this;
    }

    // Cierra la BD
    public void cierra () {
        ayuda.close();
    }



    //Insertar usuario
    public boolean insertarUsuario(String email, String password)
    {
        ContentValues initialValues = new ContentValues ();
        initialValues.put("email", email);
        initialValues.put("password", password);
        long result=bd.insert("Usuarios", null,initialValues);
        if(result ==1){return false;}
        else
            return true;
    }

    // Insertar Interlocutor
    public long insertarInterlocutor(String cardcode, String cardName, String LicTradNum, String cellular, String e_mail, String Shiptodef, String Billtodef, long id_usuario) {
        ContentValues initialValues = new ContentValues();
        initialValues.put("cardcode", cardcode);
        initialValues.put("cardName", cardName);
        initialValues.put("LicTradNum", LicTradNum);
        initialValues.put("cellular", cellular);
        initialValues.put("e_mail", e_mail);
        initialValues.put("Shiptodef", Shiptodef);
        initialValues.put("Billtodef", Billtodef);
        initialValues.put("id_usuario", id_usuario);
        return bd.insert("OCRD", null, initialValues);}

    //Intertar Contactos
    public long insertarContacto(String cardCode, String cntctPrsn, String name, String firstName, String e_mail) {
        ContentValues initialValues = new ContentValues();
        initialValues.put("CardCode", cardCode);
        initialValues.put("CntctPrsn", cntctPrsn);
        initialValues.put("Name", name);
        initialValues.put("FirstName", firstName);
        initialValues.put("E_MailL", e_mail);
        return bd.insert("OCPR", null, initialValues);}
    //Insertar Direcciones
    public long insertarDireccion(String AdresType, String Address, String Address2, String Street, String City, String State, String Country, String ZipCode) {
        ContentValues initialValues = new ContentValues();
        initialValues.put("AdresType", AdresType);
        initialValues.put("Address", Address);
        initialValues.put("Address2", Address2);
        initialValues.put("Street", Street);
        initialValues.put("City", City);
        initialValues.put("State", State);
        initialValues.put("Country", Country);
        initialValues.put("ZipCode", ZipCode);
        return bd.insert("CRD1", null, initialValues);
    }


  public Cursor obtenerUsuarios(){
      return bd.query("Usuarios", new String []
                      {"id","email", "password"},
              null,null, null, null,
              null);

  }

    public Boolean checkusername(String username){
        SQLiteDatabase MyDB = ayuda.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select * from Usuarios where email = ?", new String[]{username});
        if(cursor.getCount()>0){return true;}
        else
            return false;
    }
    public Boolean checkusernamepassword(String email, String password){
        SQLiteDatabase MyDB = ayuda.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select * from Usuarios where email = ? and password = ?", new String[]{email,password});
        if(cursor.getCount()>0){return true;}
        else
            return false;
    }


    public class AyudaDB extends SQLiteOpenHelper {

        public AyudaDB(Context con){
            super (con, DBNAME, null, VERSION);
            Log.w(TAG, "constructor de ayuda");
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                Log.w(TAG, "creando la base de datos con las tablas" );
                db.execSQL(TablaUsuario);
                db.execSQL(TablaInterlocutor);
                db.execSQL(TablaContactos);
                db.execSQL(TablaDirecciones);
            } catch (SQLException e) {
                e.printStackTrace ();
            }
        }
        @Override
        public void onUpgrade (SQLiteDatabase db,
                               int VersionAntigua, int VersionNueva) {
            Log.w(TAG, "Actualizando Base de datos de la versión" +
                    VersionAntigua + "A" + VersionNueva + ". Destruirá todos los datos");
            db.execSQL("DROP TABLE IF EXISTS TablaUsuario");
            db.execSQL("DROP TABLE IF EXISTS TablaInterlocutor");
            db.execSQL("DROP TABLE IF EXISTS TablaContactos");
            db.execSQL("DROP TABLE IF EXISTS TablaDirecciones");
            onCreate(db);
        }

    }

}
