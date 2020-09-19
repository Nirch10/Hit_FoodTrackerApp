package foodTrackerServer.lib.Models;

import com.sun.istack.internal.NotNull;
import org.hibernate.annotations.Entity;
import org.hibernate.annotations.Table;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Table(appliesTo = "FoodType")
public class FoodType {
    @Id @GeneratedValue
    @Column(name = "FoodTypeId")
    private int TypeId;
    @Column(name = "Name")
    private String Name;

    public FoodType(int typeId, @NotNull String name){
        TypeId = typeId;
        Name = name;
    }
    public FoodType(){ }
    public FoodType(String retailName) {
        Name = retailName;
    }

    public int getTypeId() {
        return TypeId;
    }
    public String getType(){
        return Name;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name){Name = name;}
    public void setTypeId(int id){
        TypeId = id;}
}
