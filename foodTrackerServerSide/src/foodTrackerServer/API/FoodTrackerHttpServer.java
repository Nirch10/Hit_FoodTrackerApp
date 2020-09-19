package foodTrackerServer.API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.istack.internal.NotNull;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import foodTrackerServer.lib.DAO.IFoodTypeDAO;
import foodTrackerServer.lib.DAO.IRecordDAO;
import foodTrackerServer.lib.DAO.IUsersDAO;
import foodTrackerServer.lib.Models.FoodType;
import foodTrackerServer.lib.Models.Record;
import foodTrackerServer.lib.Models.User;
import foodTrackerServer.lib.UsersPlatformException;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.stream.Collectors;

public class FoodTrackerHttpServer extends AbstractHttpServer<Record> {
    private RestModelConnector restModelConnector;
    private static OutputStream outputStream;
    private Gson jsonCreator;

    //C'tors
    public FoodTrackerHttpServer(@NotNull int portNum, IUsersDAO usersDAO, IFoodTypeDAO foodTypeDAO, IRecordDAO recordDAO) throws UsersPlatformException {
        this(portNum ,new RestModelConnector(usersDAO, foodTypeDAO, recordDAO));
    }

    public FoodTrackerHttpServer(int portNum, RestModelConnector restModelConnector) {
        port = portNum;
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        jsonCreator = builder.create();
        this.restModelConnector = restModelConnector;
    }

    //Public Methods
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

    }

    @Override
    public void stop() {

    }

    @Override
    public void start() throws UsersPlatformException {
        try{
            httpServer = HttpServer.create(new InetSocketAddress(port), port);
            httpServer.createContext("/", httpExchange -> {
                try {
                    defineRequestTypeAndDo(httpExchange);
                } catch (UsersPlatformException e) {
                    e.printStackTrace();
                }
            });
            httpServer.start();
        } catch (IOException e) {
            throw new UsersPlatformException("Could not start HttpServerApi - " + e.getMessage());
        }
    }

    //Http Generic
    private void responseMessage(HttpExchange httpExchange, int resCode, String data) throws UsersPlatformException {
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        httpExchange.getResponseHeaders().add("Content-Type", "application/json");
        try {
            httpExchange.sendResponseHeaders(resCode, bytes.length);
            outputStream = httpExchange.getResponseBody();
            outputStream.write(bytes);
            outputStream.close();
        } catch (IOException e) {
            throw new UsersPlatformException("Server could not response correctly, " + e.getMessage());
        }

    }

    //Declaration and definition methods
    public static String parseBody(HttpExchange httpExchange) throws IOException {
        System.out.println("trying prase body");
        return parseBodyToString(httpExchange);
    }

    private void defineRequestTypeAndDo(HttpExchange httpExchange) throws UsersPlatformException {
        String methodType = httpExchange.getRequestMethod();
        switch (methodType.toLowerCase()){
            case "get":{
                defineGetUriAndDo(httpExchange);
                break;
            }
            case
                    "post":{
                definePostUriAndDo(httpExchange);
                break;
            }
            case "delete":{
                defineDeleteUriAndDo(httpExchange);
                break;
            }
            default:
                responseMessage(httpExchange, 405, jsonCreator.toJson("Not valid request method"));
        }
    }

    private void defineGetUriAndDo(HttpExchange httpExchange) {
        String uri = httpExchange.getRequestURI().toString();
        try {
            if (uri.toLowerCase().contains("/api/home/getuserrecords"))
                getUserRecords(httpExchange);
            else if (uri.toLowerCase().contains("/api/home/getfoodtypes"))
                getFoodTypes(httpExchange);
            else if (uri.toLowerCase().contains("api/home/getrecordsbydate"))
                getRecordsByDate(httpExchange);
            else
                responseMessage(httpExchange, 404, jsonCreator.toJson("Invalid URI"));
        } catch (UsersPlatformException ex) {
            ex.printStackTrace();
        }
    }


    private void definePostUriAndDo(HttpExchange httpExchange) {
        String uri = httpExchange.getRequestURI().toString();
        try {
            if (uri.toLowerCase().contains("api/login"))
                postLogin(httpExchange);
            else if (uri.toLowerCase().contains("api/home/addrecord"))
                postRecord(httpExchange);
            else if (uri.toLowerCase().contains("api/signup"))
                postSignUp(httpExchange);
            else
                responseMessage(httpExchange, 404, jsonCreator.toJson("Invalid URI"));

        } catch (UsersPlatformException ex) {
            ex.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void defineDeleteUriAndDo(HttpExchange httpExchange) {
        String uri = httpExchange.getRequestURI().toString();
        try {
         if (uri.toLowerCase().contains("/api/home/deleterecord"))
                deleteRecord(httpExchange);
         else
             responseMessage(httpExchange, 501, jsonCreator.toJson("Invalid URI"));
         } catch (UsersPlatformException ex) {
            ex.printStackTrace();
        }
    }

    private static String parseBodyToString(HttpExchange httpExchange) throws IOException {
        try{
            System.out.println("parsing json body here");
            if(httpExchange.getRequestBody()== null)return "{}";
            InputStream requestBody = httpExchange.getRequestBody();

            StringBuilder sb = new StringBuilder();
            System.out.println("created sb" );
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(requestBody));
            System.out.println("created buffer");
            String i;int ii =0;
            while ((i = bufferedReader.readLine()) != null) {
                System.out.println("in"+ii+i);
                sb.append(i);
                ii++;
            }
            System.out.println(sb);
            if(ii==0){
                System.out.println("empty");
                return "{}";
            }
            return String.valueOf(sb);}
        catch (Exception e){
            System.out.println("error parsing body"+ e);
            return "";
        }
    }

    private User parseUserBody(HttpExchange httpExchange) throws IOException {
        String bodyStr = parseBody(httpExchange);
        Gson gson = new Gson();
        User userReceived = gson.fromJson(bodyStr, User.class);
        return userReceived;
    }

    //Get Api Methods
    private void getFoodTypes(HttpExchange httpExchange) throws UsersPlatformException {
        try {
            Collection<FoodType> foodTypes = restModelConnector.getFoodTypeDAO().getFoodTypes();
            responseMessage(httpExchange, 200, jsonCreator.toJson(foodTypes));
        } catch (UsersPlatformException e) {
            responseMessage(httpExchange, 404, jsonCreator.toJson(e.getMessage()));
        }
    }

    private void getUserRecords(HttpExchange httpExchange) throws UsersPlatformException {
        String[] uri = httpExchange.getRequestURI().toString().split("/");
        int id = Integer.parseInt(uri[uri.length - 1]);
        try {
            Collection<Record> userRecords = restModelConnector.getRecordsDAO().getUserRecords(id);
            String j = jsonCreator.toJson(userRecords);
            responseMessage(httpExchange, 200, j);
        } catch (UsersPlatformException e) {
            responseMessage(httpExchange, 404, jsonCreator.toJson(e.getMessage()));
        }
        catch (Exception e){
            responseMessage(httpExchange, 404, jsonCreator.toJson(e.getMessage()));
        }
    }

    private void getRecordsByDate(HttpExchange httpExchange) throws UsersPlatformException {
        String[] uriParams = httpExchange.getRequestURI().getQuery().split("&");
        try {

            String[] userId = uriParams[0].split("=");
            String[] fromDateStrArray = uriParams[1].split("=");
            String[] toDateStrArray = uriParams[2].split("=");
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date fromDate = format.parse(fromDateStrArray[1]);
            Date toDate = format.parse(toDateStrArray[1]);

            Collection<Record> recordsByDate = restModelConnector.getRecordsDAO().getUserRecords(fromDate, toDate,Integer.parseInt(userId[1]));
            String j = jsonCreator.toJson(recordsByDate);
            responseMessage(httpExchange, 200, j);
        } catch (UsersPlatformException e) {
            responseMessage(httpExchange, 404, jsonCreator.toJson(e.getMessage()));
        } catch (ParseException e) {
            responseMessage(httpExchange, 404, jsonCreator.toJson(e.getMessage()));
        }
    }

    //Post Api Methods
    private void postRecord(HttpExchange httpExchange) throws IOException, UsersPlatformException {
        String bodyStr = parseBody(httpExchange);
        Record recordToAdd = initRecord(bodyStr);
        try{
            restModelConnector.getRecordsDAO().addRecord(recordToAdd);
            responseMessage(httpExchange, 200, jsonCreator.toJson(recordToAdd));
        } catch (UsersPlatformException e) {
            responseMessage(httpExchange, 400, jsonCreator.toJson("record's incorrect"));
        } catch (SQLException e) {
            responseMessage(httpExchange, 400, jsonCreator.toJson("records's incorrect"));
        }
    }

    private Record initRecord(String recordJsonStr) {
        Record recordToAdd = jsonCreator.fromJson(recordJsonStr, Record.class);
        if(recordToAdd == null)
            return null;
        if(recordToAdd.getDateOfRecord() == null)
            recordToAdd.setDateOfRecord(new Date());
        try {
            recordToAdd.setFoodType(restModelConnector.getFoodTypeDAO().getFoodTypeId(recordToAdd.getFoodType().getTypeId()));
            recordToAdd.setUser(restModelConnector.getUsersDAO().getUser(recordToAdd.getUser().getUserId()));
            return recordToAdd;
        } catch (UsersPlatformException e) {
            return null;
        }
    }

    private void postLogin(HttpExchange httpExchange) throws IOException, UsersPlatformException {
        User userReceived = parseUserBody(httpExchange);
        if(userReceived.getUserName() == null || userReceived.getPassword() == null){
            responseMessage(httpExchange, 401, jsonCreator.toJson("Incorrect Parameters"));
            return;
        }
        try{
            User user = restModelConnector.getUsersDAO().getUser(userReceived.getUserName());
            if(userReceived.getPassword().equals(user.getPassword())&& userReceived.getUserName().toLowerCase()
                    .equals(user.getUserName().toLowerCase()))
                responseMessage(httpExchange, 200, jsonCreator.toJson(user));
            else
                responseMessage(httpExchange, 404, jsonCreator.toJson("Incorrect Password"));
        } catch (UsersPlatformException e) {
            responseMessage(httpExchange, 401, jsonCreator.toJson("No Such user found"));
        }
    }

    private void postSignUp(HttpExchange httpExchange) throws IOException, UsersPlatformException {
        User userReceived = parseUserBody(httpExchange);
        if(userReceived.getUserName() == null || userReceived.getPassword() == null){
            responseMessage(httpExchange, 400, jsonCreator.toJson("Incorrect Parameters"));
            return;
        }
        try {
            User user = restModelConnector.getUsersDAO().getUser(userReceived.getUserName());
            if (user == null) {
                user = new User(userReceived.getUserName(), userReceived.getPassword());
                restModelConnector.getUsersDAO().addUser(user);
                responseMessage(httpExchange, 200, jsonCreator.toJson(user));
            }
            else
                responseMessage(httpExchange, 404, jsonCreator.toJson("User exists"));
        }
       catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //Delete Api Methods
    private void deleteRecord(HttpExchange httpExchange) throws UsersPlatformException {
        String[] uri = httpExchange.getRequestURI().toString().split("/");
        int id = Integer.parseInt(uri[uri.length - 1]);
        try {
            restModelConnector.getRecordsDAO().deleteRecord(id);
            responseMessage(httpExchange, 200, "Res : record deleted successfully");
        } catch (UsersPlatformException e) {
            responseMessage(httpExchange, 404, jsonCreator.toJson(e.getMessage()));
        } catch (SQLException e) {
            responseMessage(httpExchange, 404, jsonCreator.toJson(e.getMessage()));
        }
    }




}
