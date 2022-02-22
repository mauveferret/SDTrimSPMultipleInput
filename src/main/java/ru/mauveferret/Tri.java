package ru.mauveferret;

public class Tri {

    private double energy, angle; //energy in keV
    private int ID, counts; // counts in thousands
    private String projectile = "D", solid = "FeCrNiTi";

    public Tri(int ID, double energy, double angle, int counts){
        this.ID = ID;
        this.energy = energy;
        this.angle = angle;
        this. counts = counts;
    }



    public String getDir(){
        String dir = ID+"_"+projectile+angle+"deg"+energy+"keV_"+solid;
        dir+=((counts%1000==0) ? (counts/1000)+"M" : counts+"k");
        return  dir.replaceAll("\\.",",");
    }

    public String createTri(){
       String tri =  energy+" keV "+projectile+" -> "+solid+" "+angle+" degrees\n";
       tri = tri.replaceAll("\\.",",");
       tri+="   &TRI_INP\n" +
               "text='---elemements---'     \n" +
               "     ncp = 5\n" +
               "     symbol =\"D\",\"Fe\",\"Cr\",\"Ni\",\"Ti\"\n" +
               "     \n     nh       =   ";
       tri+=counts;
       tri+="\n     idout    =   1000000\n" +
               "     nr_pproj = 1000\n" +
               "     \n" +
               "\t iintegral=0\n" +
               "     idrel = 1\n" +
               "     isbv  = 1\n" +
               "     ipot  = 1\n" +
               "     imcp  = 1\n" +
               "     inel0 = 2\n" +
               "\t\n" +
               "text='---beam---' \n" +
               "     qubeam = 1, 0, 0, 0, 0     \n" +
               "     case_e0=0\n";
       tri+="     e0     =  "+(energy*1000)+", 0, 0, 0, 0\n\n";
       tri+="     case_alpha=0\n";
       tri+="     alpha0 =   "+angle+", 0, 0, 0, 0\n";
       tri+="     \n" +
               "text='---target---'\n" +
               "     ttarget = 2000 \n" +
               "     nqx     = 500 \n" +
               "     qu      = 0, 0.674, 0.179, 0.101, 0.046\n" +
               "\t \n" +
               "\t irc0=1\n" +
               "       \n" +
               "     numb_hist = 1    \n" +
               "\t number_calc=1\n" +
               "\t \n" +
               "\t lenergy_distr=.true.\n" +
               "     lparticle_p=.true.\n" +
               "     lparticle_r=.true.\n" +
               "\t ioutput_hist = 2\n" +
               "     ioutput_part=1000,100000000,1000,100000,1000000000,1000\n" +
               "\t lmatrices=.true.\n" +
               "\t lmoments=.true.\n" +
               "/\n";
       return tri;
    }
    
}
