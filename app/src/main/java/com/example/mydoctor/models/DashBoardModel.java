
package com.example.mydoctor.models;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashBoardModel implements Serializable, Parcelable
{

    @SerializedName("countVisits")
    @Expose
    private Integer countVisits;
    @SerializedName("countHealthRate")
    @Expose
    private Integer countHealthRate;
    @SerializedName("CountMedicine")
    @Expose
    private Integer countMedicine;
    @SerializedName("countMhospitals")
    @Expose
    private Integer countMhospitals;
    @SerializedName("error_message")
    @Expose
    private String error_message;
    public final static Parcelable.Creator<DashBoardModel> CREATOR = new Creator<DashBoardModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public DashBoardModel createFromParcel(Parcel in) {
            return new DashBoardModel(in);
        }

        public DashBoardModel[] newArray(int size) {
            return (new DashBoardModel[size]);
        }

    }
    ;
    private final static long serialVersionUID = -8904758399187788120L;

    protected DashBoardModel(Parcel in) {
        this.countVisits = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.countHealthRate = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.countMedicine = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.countMhospitals = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public DashBoardModel() {
    }

    /**
     * 
     * @param countHealthRate
     * @param countVisits
     * @param countMhospitals
     * @param countMedicine
     */
    public DashBoardModel(Integer countVisits, Integer countHealthRate, Integer countMedicine, Integer countMhospitals) {
        super();
        this.countVisits = countVisits;
        this.countHealthRate = countHealthRate;
        this.countMedicine = countMedicine;
        this.countMhospitals = countMhospitals;
    }

    public Integer getCountVisits() {
        return countVisits;
    }

    public void setCountVisits(Integer countVisits) {
        this.countVisits = countVisits;
    }

    public Integer getCountHealthRate() {
        return countHealthRate;
    }

    public void setCountHealthRate(Integer countHealthRate) {
        this.countHealthRate = countHealthRate;
    }

    public Integer getCountMedicine() {
        return countMedicine;
    }

    public void setCountMedicine(Integer countMedicine) {
        this.countMedicine = countMedicine;
    }

    public Integer getCountMhospitals() {
        return countMhospitals;
    }

    public void setCountMhospitals(Integer countMhospitals) {
        this.countMhospitals = countMhospitals;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(countVisits);
        dest.writeValue(countHealthRate);
        dest.writeValue(countMedicine);
        dest.writeValue(countMhospitals);
    }

    public int describeContents() {
        return  0;
    }

}
