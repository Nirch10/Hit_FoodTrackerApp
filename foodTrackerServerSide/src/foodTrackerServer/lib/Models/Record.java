package foodTrackerServer.lib.Models;

import com.sun.istack.internal.NotNull;
import org.hibernate.annotations.Entity;
import org.hibernate.annotations.Table;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(appliesTo = "Record")
public class Record {
    @Id @GeneratedValue
    @Column(name = "RecordId")
    private int RecordId;
    @Column(name = "Calories")
    private int Calories;
    @Column(name = "FoodTypeId")
    private FoodType FoodType;
    @Column(name = "UserGuid")
    private User User;
    @Column(name = "DateOfRecord", columnDefinition = "DATE")
    private Date DateOfRecord;
    @Column(name = "Description")
    private String Description;

    public Record(@NotNull int recordId, int calories, @NotNull foodTrackerServer.lib.Models.FoodType foodType, @NotNull  User user,
                  Date date, String description){
        //TODO::: Generate id automatically
        RecordId = recordId;
        Calories = calories;
        FoodType = foodType;
        User = user;
        DateOfRecord = date;
        Description = description;
    }
    public Record() { }

    public int getRecordId() {
        return RecordId;
    }
    public int getCalories() {
        return Calories;
    }
    public foodTrackerServer.lib.Models.FoodType getFoodType() {
        return FoodType;
    }
    public User getUser() {
        return User;
    }
    public Date getDateOfRecord() {
        return DateOfRecord;
    }
    public String getDescription() {
        return Description;
    }
    public void setRecordId(int recordId){ RecordId = recordId;}
    public void setCalories(int calories){ Calories = calories;}
    public void setFoodType(foodTrackerServer.lib.Models.FoodType foodType){
        FoodType = foodType;}
    public void setUser(User user){User = user;}
    public void setDateOfRecord(Date dateOfRecord){
        DateOfRecord = dateOfRecord;}
    public void setDescription(String description){Description = description;}
}