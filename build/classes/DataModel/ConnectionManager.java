/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataModel;

import java.lang.reflect.Constructor;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bastien
 */
public class ConnectionManager {

    private final static String url = "jdbc:mysql://localhost:3305/pascard";
    private final static String driverName = "com.mysql.jdbc.Driver";
    private final static String username = "pascard-rw";
    private final static String password = "oBcTvODy";
    private static Connection con;
    private static String usernameECE;
    private static String passwordECE;
    private static HashMap<Class, String> table_map; //Mapping entre les classes de DataModel et les tables de la BDD
    private static PreparedStatement pstmt_select_simple;
    
    private static void init_table_map()
    {
        table_map=new HashMap();
        table_map.put(Game.class, "games");
        table_map.put(Move.class, "moves");
        table_map.put(Player.class, "users");
    }
    
    static private void prepare_stmt() throws SQLException
    {
        pstmt_select_simple = con.prepareStatement("SELECT * FROM ? WHERE ?=?");
        
    }
    
    static public void resetDB(String reset_query){
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(reset_query);
        } catch (Exception ex) {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    
    private static String getTable(Class type)
    {
        return table_map.get(type);
    }
    
    public static Connection getConnectionLocal() throws SQLException, ClassNotFoundException{
        init_table_map();
        //String l_url = "jdbc:mysql://localhost:8889/chessdb";
        String l_url = "jdbc:mysql://192.168.43.228:3306/chessdb";
        String l_username = "wechess";
        String l_password = "wechess";
        
        try {
            Class.forName(driverName);
            
            con = DriverManager.getConnection(l_url, l_username, l_password);
        } catch (SQLException ex) {
            // log an exception. fro example:
            ex.printStackTrace();
            System.out.println("Failed to create the database connection.");
            throw ex;
        } catch (ClassNotFoundException ex) {
            // log an exception. for example:
            System.out.println("Driver not found.");
            throw ex;
        }
        System.out.println("MySQL connexion successful : " + l_url);
        return con;
    }
    
    public static Connection getConnection() throws SQLException, ClassNotFoundException{
        init_table_map();
        /*
        Scanner input = new Scanner(System.in);
        System.out.println("login ECE : ");
        usernameECE = input.nextLine();
        System.out.println("mdp ECE : ");
        passwordECE = input.nextLine();
        */
        usernameECE = "pascard";
        
        String key = "azertyuiop";
        int[] mdp = {-31, -25, -55, -34, -19, -75, -12, 5, -8, -63};
        String r_m = "";
        for(int i = 0; i<key.length() ; i++){
            r_m +=(char)(mdp[i]+key.charAt(i));
        }
        
        passwordECE = r_m;
        
        try {
            Class.forName(driverName);
            SSHTunnel ssh = new SSHTunnel(usernameECE, passwordECE);

            ssh.connect();
            con = DriverManager.getConnection(url, username, password);
        } catch (SQLException ex) {
            // log an exception. fro example:
            ex.printStackTrace();
            System.out.println("Failed to create the database connection.");
            throw ex;
        } catch (ClassNotFoundException ex) {
            // log an exception. for example:
            System.out.println("Driver not found.");
            throw ex;
        }
        System.out.println("MySQL connexion successful : " + url);
        return con;
    }

    private static ArrayList<HashMap<String, String>> select(String requete) throws SQLException {
        
        ArrayList<HashMap<String, String>> table = new ArrayList();
        HashMap<String, String> ligne = new HashMap();

        // System.out.println("Requete Recherche : " + requete);
        Statement stmt = con.createStatement();
        ResultSet rset = stmt.executeQuery(requete);
        
        ResultSetMetaData rsetMeta = rset.getMetaData();
        int nbColonne = rsetMeta.getColumnCount();

        ArrayList<String> fields = new ArrayList();
        
        for (int i = 1; i <= nbColonne; i++) {
            fields.add(rsetMeta.getColumnLabel(i));
        }

        while (rset.next()) 
        {
            ligne = new HashMap();
            for (int i = 0; i < nbColonne; i++) {
                ligne.put(fields.get(i), rset.getString(i + 1));
            }
            table.add(ligne);
        }
        return table;
    }

    private static ArrayList<HashMap<String, String>> select(Class type, HashMap<String, String> data){
        String query = "SELECT * FROM "+getTable(type)+" WHERE ";
        boolean first=true;
        ArrayList<HashMap<String, String>> raw_results;
        ArrayList<DataModel> results = new ArrayList();
        
        for (Map.Entry<String, String> entry : data.entrySet())
        {
            if(first){
                first=false;
            }
            else{
                query = query + " AND ";
            }
            query = query + entry.getKey() + "='" + entry.getValue() + "' ";
        }
        try{
            raw_results = select(query);
            
            return raw_results;
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    
    //requete de création d'objet, retourne l'id attribué a l'objet par la BDD
    public static String insert(Class type, HashMap<String, String> data) {
        try {
            ArrayList<String> field_names=new ArrayList();
            ArrayList<String> values=new ArrayList();
            
            String query = "INSERT INTO "+getTable(type)+"(";
            String id="-1";
            
            //on extrait les noms des champs et leurs valeurs
            for (Map.Entry<String, String> entry : data.entrySet()) {
                if(entry.getKey().equals("id")) continue;
                field_names.add(entry.getKey());
                values.add(entry.getValue());
            }
            
            //on compose la requete SQL
            for (String field : field_names){
                query=query.concat(field+",");
            }
            query=query.substring(0,query.length()-1);
            query=query.concat(") values (");
            for (String s : values){
                query = query.concat("'"+s+"',");
            }
            query=query.substring(0,query.length()-1);
            query =query.concat(");");
            
            //afficher la requete
            //System.out.println("Requete creation : " + query);
            
            //executer la requete d'insertion
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
            
            //on recupere l'id de l'objet cree
            ArrayList<HashMap<String, String>> result = select("SELECT LAST_INSERT_ID() FROM "+getTable(type));
            id=result.get(0).get("LAST_INSERT_ID()").toString();
            if(!stmt.isClosed()) stmt.close();
            return id;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return null;
    }
    
    //met la valeur value le champ field de l'objet portant l'id id dans la table table
    public static boolean update(Class type, String id, String field, Object value) {
        
        try {
            String query = "UPDATE "+getTable(type)+" SET "+field+"='"+value.toString()+"' WHERE id="+id+";";
            Statement stmt = con.createStatement();
            //afficher la requete
            //System.out.println("Requete MAJ : " + query);
            //executer la modification
            stmt.executeUpdate(query);
            if(stmt!=null) stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return true;
    }
    
    //renvoie tous les objets ou l'attribut field=value
    public static ArrayList<DataModel> find(Class type, HashMap<String, String> param){
        try {
            ArrayList<DataModel> results = new ArrayList();
            String table = table_map.get(type);
            
            ArrayList<HashMap<String, String>> raw_results = ConnectionManager.select(type, param);
            
            for(HashMap<String, String> data : raw_results){
                
                Constructor constr = type.getConstructor(HashMap.class);
                results.add((DataModel)constr.newInstance(data));
            }
            
            return results;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    //renvoie tous les objets ou l'attribut field=value
    public static ArrayList<DataModel> find(Class type, String field, Object value)
    {
        HashMap<String, String> critere = new HashMap();
        critere.put(field, value.toString());
        return find(type,critere);
    }
    
    //renvoie l'objet DataModel de type "type" dont l' "id" est id
    public static DataModel findById(Class type, Object id) throws SQLException
    {
        ArrayList<DataModel> d = find(type,"id",id);
        if(!d.isEmpty())
        return (DataModel)d.get(0);
        else throw new SQLException("No object of type "+type.getSimpleName()+"found for id "+id);
    }
    
}
