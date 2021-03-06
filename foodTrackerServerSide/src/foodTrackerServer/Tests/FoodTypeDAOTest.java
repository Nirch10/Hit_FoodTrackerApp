package foodTrackerServer.Tests;

import foodTrackerServer.Config.FoodTrackerConfigWrapper;
import foodTrackerServer.lib.DAO.HibernateFoodTypeDAO;
import foodTrackerServer.lib.DAO.IFoodTypeDAO;
import foodTrackerServer.lib.Models.FoodType;
import foodTrackerServer.lib.UsersPlatformException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

public class FoodTypeDAOTest {
    static IFoodTypeDAO tester;
    @BeforeClass
    public static void testSetup() throws IOException {
        tester = new HibernateFoodTypeDAO(FoodTrackerConfigWrapper.Deserialize("./Config.json"));
    }
    @AfterClass
    public static void testCleanup() {
        // Do your cleanup here like close URL connection , releasing resources etc
    }

    //Get tests
    @Test
    public void getFoodType() throws Exception {
        int retailGuid = 1;
        FoodType result = tester.getFoodTypeId(retailGuid);
        if (result == null) throw new AssertionError();
        System.out.println(result.getTypeId() +" : "+result.getName());
    }
    @Test
    public void getFoodTypes() throws UsersPlatformException {
        Collection<FoodType> cl = tester.getFoodTypes();
        if(cl.size() == 0) throw new AssertionError("empty list");
        cl.forEach(c -> System.out.println(c.getTypeId() +" : "+ c.getType()));
    }
    //Insert Tests
    @Test
    public void addFoodType(){
        String retailName = "Sports";
        try{
            tester.addFoodType(new FoodType(retailName));
        } catch (UsersPlatformException e) {
            throw new AssertionError("Retail insertion failure");
        } catch (SQLException e) {
            throw new AssertionError("Retail insertion failure");
        }

    }
    @Test
    public void testInsertRetails(){
        String[] retailNames = {"None","Beverage", "Meat", "Snacks", "Fruits", "Dairy", "Vegan","Sweets","Sea Food","Other"};

        for (int i = 0; i< retailNames.length;i++){
            try{
                tester.addFoodType(new FoodType(retailNames[i]));
            } catch (UsersPlatformException e) {
                throw new AssertionError("Type insertion failed");
            } catch (SQLException e) {
                throw new AssertionError("Type insertion failed");
            }
        }
    }
    //Delete Tests
    @Test
    public void testRemoveFoodType(){
        try {
            for(int i = 2;i<10;i++)
                tester.deleteFoodType(i);
        } catch (UsersPlatformException e) {
            throw new AssertionError(e.getMessage());
        } catch (SQLException e) {
            throw new AssertionError(e.getMessage());
        }
    }
    //Update tests
    @Test
    public void testUpdateFoodType(){
        String retailsNewName = "None";
        try {
            tester.setFoodTypeName(1,retailsNewName);
        } catch (SQLException e) {
            throw new AssertionError(e.getMessage());
        } catch (UsersPlatformException e) {
            throw new AssertionError(e.getMessage());
        }
    }
}
