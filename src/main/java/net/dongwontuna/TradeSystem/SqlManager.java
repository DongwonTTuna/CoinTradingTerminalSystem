package net.dongwontuna.TradeSystem;

import java.io.*;
import java.sql.*;
import java.util.HashMap;

public class SqlManager {
    private static Connection conn;
    private static Statement stat;

    static {

        File file = new File("database.db");
        if (!file.exists()) {
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                // Open the resource as an input stream
                inputStream = SqlManager.class.getResourceAsStream("/data.db");

                // Create the output stream
                outputStream = new FileOutputStream("database.db");

                // Copy the file
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Close the streams
                try {
                    if (inputStream != null) inputStream.close();
                    if (outputStream != null) outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            // Check SQLite JDBC is Loaded.
            Class.forName("org.sqlite.JDBC");

            // Test connection to DatabaseFile.
            conn = DriverManager.getConnection("jdbc:sqlite:database.db");
            stat = conn.createStatement();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    public static void setAPI(String exchange,String APIKEY, String PRIVKEY){
        try{
            PreparedStatement st = conn.prepareStatement("INSERT OR REPLACE INTO APIKEY (EXCHANGE, API_KEY, SECRET_KEY) VALUES (?, ?, ?)");
            st.setString(1,exchange);
            st.setString(2, APIKEY);
            st.setString(3, PRIVKEY);
            st.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static HashMap<String,String> getAPI(String exchange){
        try {
            ResultSet rs = stat.executeQuery("SELECT * FROM APIKEY WHERE EXCHANGE = '" + exchange + "'");
            HashMap<String, String> tempHashMap = new HashMap<>();
            if (rs.next()) {
                tempHashMap.put("API_KEY", rs.getString("API_KEY"));
                tempHashMap.put("SECRET_KEY", rs.getString("SECRET_KEY"));
            }
            return tempHashMap;
        } catch (SQLException e) {
           throw new RuntimeException(e);
        }
    }

    public static void getDailyData(){

    }
    public static void getOrders(){

    }



}
