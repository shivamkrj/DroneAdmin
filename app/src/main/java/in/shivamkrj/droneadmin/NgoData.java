package in.shivamkrj.droneadmin;

public class NgoData {
    public String ngoName;
    public String userName;
    public String phoneNUmber;
    public String  detail;
    public NgoData(String ngoName,String userName ,String phoneNUmber,String detail){
        this.detail=detail;
        this.ngoName=ngoName;
        this.phoneNUmber=phoneNUmber;
        this.userName=userName;
    }
    public NgoData(){

    }
}
