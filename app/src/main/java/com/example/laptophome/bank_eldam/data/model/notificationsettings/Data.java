
package com.example.laptophome.bank_eldam.data.model.notificationsettings;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("governorates")
    @Expose
    private List<Object> governorates = null;
    @SerializedName("blood_types")
    @Expose
    private List<String> bloodTypes = null;

    public List<Object> getGovernorates() {
        return governorates;
    }

    public void setGovernorates(List<Object> governorates) {
        this.governorates = governorates;
    }

    public List<String> getBloodTypes() {
        return bloodTypes;
    }

    public void setBloodTypes(List<String> bloodTypes) {
        this.bloodTypes = bloodTypes;
    }

}
