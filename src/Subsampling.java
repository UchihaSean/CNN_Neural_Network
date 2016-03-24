/**
 * Created by Jly_wave on 10/22/15.
 */
public class Subsampling {
    private double[][][] pool_Map,layer_Adjust;
    private int[][][] max_Refer;
    private int number,scale,kernel_Scale;
    public Subsampling(int number,int scale,int kernel_Scale){
        this.number=number;
        this.scale=scale;
        this.kernel_Scale=kernel_Scale;
        pool_Map=new double[number][scale][scale];
        layer_Adjust=new double[number][scale][scale];
        max_Refer=new int[number][scale][scale];
        for (int i=0;i<number;i++)
            for (int j=0;j<scale;j++)
                for (int k=0;k<scale;k++) {
                    layer_Adjust[i][j][k]=0;
                }
        initial();
    }
    public void initial(){
        for (int i=0;i<number;i++)
            for (int j=0;j<scale;j++)
                for (int k=0;k<scale;k++) {
                    pool_Map[i][j][k] = 0;
                    max_Refer[i][j][k]=0;
                    layer_Adjust[i][j][k]=0;
                }
    }
    //test pass
    public void set_Layer(int num,double[][] upper){
        for (int i=0;i<scale;i++)
            for (int j=0;j<scale;j++){
                pool_Map[num][i][j]=max(num,i,j,upper[i*kernel_Scale][j*kernel_Scale],upper[i*kernel_Scale][j*kernel_Scale+1],
                                                 upper[i*kernel_Scale+1][j*kernel_Scale],upper[i*kernel_Scale+1][j*kernel_Scale+1]);
            }
    }
    //test pass
    public void set_Layer_Adjust(int num,int below_Scale,int below_Kernel_Scale,double[][] below_Layer_Adjust,double[][] below_Kernel,double[][] below_Layer){
        int expand_Below_Scale=below_Kernel_Scale+scale-1;
        double[][] expand_Below=new double[expand_Below_Scale][expand_Below_Scale];
        double[][] expand_Belowx=new double[expand_Below_Scale][expand_Below_Scale];
        for (int i=0;i<expand_Below_Scale;i++)
            for (int j=0;j<expand_Below_Scale;j++){
                expand_Below[i][j]=0;
                expand_Belowx[i][j]=0;
            }
        int t=(expand_Below_Scale-below_Scale)/2;
        for (int i=t;i<t+below_Scale;i++)
            for (int j=t;j<t+below_Scale;j++){
                expand_Below[i][j]=below_Layer_Adjust[i-t][j-t];
                expand_Belowx[i][j]=below_Layer[i-t][j-t];
            }
        double[][] rot_Below_Kernel=new double[below_Kernel_Scale][below_Kernel_Scale];
        for (int i=0;i<below_Kernel_Scale;i++)
            for (int j=0;j<below_Kernel_Scale;j++){
                rot_Below_Kernel[i][j]=below_Kernel[below_Kernel_Scale-i-1][below_Kernel_Scale-j-1];
//                rot_Below_Kernel[i][j]=below_Kernel[i][j];
            }
        for (int i=0;i<scale;i++)
            for (int j=0;j<scale;j++){
                for (int k1 = 0; k1<below_Kernel_Scale;k1++)
                    for (int k2=0;k2<below_Kernel_Scale;k2++){
                        layer_Adjust[num][i][j]+=drelu(expand_Belowx[i+k1][j+k2])*expand_Below[i+k1][j+k2]*rot_Below_Kernel[k1][k2];
                    }
            }
    }

    public double[][] get_Layer(int num){
        return pool_Map[num];
    }
    public double[][] get_Layer_Adjust(int num){
        return layer_Adjust[num];
    }
    public int[][] get_Max_Refer(int num){
        return max_Refer[num];
    }
    private double max(int num,int i,int j,double a,double b,double c,double d){
        double max_Value=a;
        int refer=0;
        if (b>max_Value){
            max_Value=b;
            refer=1;
        }
        if (c>max_Value){
            max_Value=c;
            refer=2;
        }
        if(d>max_Value){
            max_Value=d;
            refer=3;
        }
        max_Refer[num][i][j]=refer;
        return max_Value;
    }
    private double drelu(double x){
        if (x<=0) return 0;
        return 1;
    }

}
