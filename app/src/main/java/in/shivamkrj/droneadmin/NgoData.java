package in.shivamkrj.droneadmin;

public class NgoData {
    public String ngoName;
    public String userName;
    public String phoneNUmber;
    public String  detail;
    public String key;
    public NgoData(String ngoName,String userName ,String phoneNUmber,String detail,String key){
        this.detail=detail;
        this.ngoName=ngoName;
        this.phoneNUmber=phoneNUmber;
        this.userName=userName;
        this.key = key;
    }
    public NgoData(){

    }
}
