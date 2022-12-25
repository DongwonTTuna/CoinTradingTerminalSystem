package net.dongwontuna.TradeSystem;

import org.knowm.xchange.dto.marketdata.Trade;

import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class SqlManager {
    private static Connection conn;
    private static Statement stat;



    static {
        createDBFile();
    }

    private static void createDBFile(){
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

    public static boolean deleteDBFile(){
        File file = new File("database.db");
        if (file.delete()) createDBFile();
        else return false;
        return true;
    }
    public static void setAPI(String exchange,String APIKEY, String PRIVKEY){
        try{
            PreparedStatement st = conn.prepareStatement("INSERT OR REPLACE INTO APIKEY VALUES (?, ?, ?)");
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
    public static void setDailyData(String Date, String TotalBalance,String TradeNum, String TodayPNL){
        try{
            PreparedStatement st = conn.prepareStatement("INSERT OR REPLACE INTO DAILYDATA VALUES (?, ?, ?, ?)");
            st.setInt(1,Integer.parseInt(Date));
            st.setBigDecimal(2, new BigDecimal(TotalBalance));
            st.setInt(3,Integer.parseInt(TradeNum));
            st.setBigDecimal(4, new BigDecimal(TodayPNL));
            st.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static HashMap<String,Object> getDailyData(String Date){
        try {
            ResultSet rs = stat.executeQuery("SELECT * FROM DAILYDATA WHERE DATE = '" + Date + "'");
            HashMap<String, Object> tempHashMap = new HashMap<>();
            if (rs.next()) {
                tempHashMap.put("DATE", rs.getInt("DATE"));
                tempHashMap.put("TOTAL_BALANCE", rs.getBigDecimal("TOTAL_BALANCE"));
                tempHashMap.put("TRADE_NUM", rs.getInt("TRADE_NUM"));
                tempHashMap.put("TODAY_PNL",rs.getBigDecimal("TODAY_PNL"));
            }
            return tempHashMap;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setOrder(String OrderNum, String Exchange, String Ticker, String OrderType, BigDecimal TriggerPrice, BigDecimal TargetPrice, BigDecimal Amount){
        try{
            PreparedStatement st = conn.prepareStatement("INSERT OR REPLACE INTO ORDERES VALUES (?, ?, ?, ?, ?, ?, ?)");
            st.setInt(1,Integer.parseInt(OrderNum));
            st.setString(2, Exchange);
            st.setString(3,Ticker);
            st.setString(4, OrderType);
            st.setBigDecimal(5, TriggerPrice);
            st.setBigDecimal(6, TargetPrice);
            st.setBigDecimal(7, Amount);
            st.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
    public static ArrayList<HashMap<String,Object>> getOrders(){
        try {
            ResultSet rs = stat.executeQuery("SELECT * FROM ORDERS");
            ArrayList<HashMap<String,Object>> parentArrayList = new ArrayList<>();
            while (rs.next()) {
                HashMap<String, Object> childHashMap = new HashMap<>();
                childHashMap.put("ORDER_NUM", rs.getInt("ORDER_NUM"));
                childHashMap.put("EXCHANGE", rs.getString("EXCHANGE"));
                childHashMap.put("TICKER", rs.getString("TICKER"));
                childHashMap.put("ORDER_TYPE", rs.getInt("ORDER_TYPE"));
                childHashMap.put("TRIGGER_PRICE", rs.getBigDecimal("TRIGGER_PRICE"));
                childHashMap.put("TARGET_PRICE", rs.getBigDecimal("TARGET_PRICE"));
                childHashMap.put("AMOUNT", rs.getBigDecimal("AMOUNT"));
                parentArrayList.add(childHashMap);
            }
            return parentArrayList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
