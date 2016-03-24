import java.sql.Connection;

/**
 * Created by Jly_wave on 10/22/15.
 */
public class Convolution {
    private double[][][] feature_Map,kernel_Map,layer_Adjust,kernel_Adjust,sum_Kernel_Adjust;
    private double[] bias_Adjust,bias,sum_Bias_Adjust;
    private int kernel_Scale,scale,number;
    public Convolution(int number,int scale,int kernel_Scale){
        feature_Map=new double[number][scale][scale];
        kernel_Map=new double[number][kernel_Scale][kernel_Scale];
        layer_Adjust=new double[number][scale][scale];
        kernel_Adjust=new double[number][kernel_Scale][kernel_Scale];
        sum_Kernel_Adjust=new double[number][kernel_Scale][kernel_Scale];
        bias_Adjust=new double[number];
        sum_Bias_Adjust=new double[number];
        bias=new double[number];
        this.kernel_Scale=kernel_Scale;
        this.scale=scale;
        this.number=number;
        for (int i=0;i<number;i++)
            for (int j=0;j<kernel_Scale;j++) {
                //test
//                kernel_Map[i][j][0]=1.0/kernel_Scale;
                for (int k = 0; k < kernel_Scale; k++) {
                    kernel_Map[i][j][k] = (Math.random() * 2 - 1) / kernel_Scale;
                    //test
//                    kernel_Map[i][j][k]=-kernel_Map[i][j][k-1];
                    //
                }
            }
        //test
//        bias[0]=1;
        for (int i=0;i<number;i++) {
            bias[i] = -1 + 2 * Math.random();
            //test
//            bias[i]=-bias[i-1];
            //
        }
        for (int i=0;i<number;i++)
            for (int j=0;j<kernel_Scale;j++)
                for (int k=0;k<kernel_Scale;k++){
                    kernel_Adjust[i][j][k]=0;
                    sum_Kernel_Adjust[i][j][k]=0;
                }
        for (int i=0;i<number;i++) {
            bias_Adjust[i] = 0;
            sum_Bias_Adjust[i]=0;
        }
        for (int i=0;i<number;i++)
            for (int j=0;j<scale;j++)
                for (int k=0;k<scale;k++){
                    layer_Adjust[i][j][k]=0;
                }
        initial();
    }
    public void initial(){
        for (int i=0;i<number;i++)
            for (int j=0;j<scale;j++)
                for (int k=0;k<scale;k++){
                    feature_Map[i][j][k]=0;
                    layer_Adjust[i][j][k]=0;
                }
        for (int i=0;i<number;i++)
            for (int j=0;j<kernel_Scale;j++)
                for (int k=0;k<kernel_Scale;k++){
                    kernel_Adjust[i][j][k]=0;
                }
        for (int i=0;i<number;i++){
            bias_Adjust[i]=0;
        }
    }
    //test pass
    public void set_Layer(int num,double[][] upper){
        for (int i=0;i<scale;i++)
            for (int j=0;j<scale;j++){
                for (int k1=0;k1<kernel_Scale;k1++)
                    for (int k2=0;k2<kernel_Scale;k2++){
                        feature_Map[num][i][j]+=upper[i+k1][j+k2]*kernel_Map[num][k1][k2];
                    }
            }

    }
    //test pass
    public void set_Bias_Sigm(int num){
        for (int i=0;i<scale;i++)
            for (int j=0;j<scale;j++)
                feature_Map[num][i][j] = relu(feature_Map[num][i][j]+bias[num]);
    }
    //test
    public void set_Layer_Adjust(int[] standard){
        for (int i=0;i<3;i++)
            layer_Adjust[i][0][0]=standard[i]-feature_Map[i][0][0];
    }
    public void set_Layer(){
        feature_Map[0][0][0]=1;
        for (int i=1;i<5;i++){
            feature_Map[i][0][0]=-feature_Map[i-1][0][0];
        }
    }
    //
    public void set_Layer_Adjust_Bp(int now,double down_Layer_Adjust,double down_Layer,double down_Weight){
        layer_Adjust[now][0][0]+=drelu(down_Layer)*down_Layer_Adjust*down_Weight;
    }
    //test pass
    public void set_Layer_Adjust_Sub(int num,int down_Kernel_Scale,int[][] max_Refer,double[][] down_Layer_Adjust){
        int k1=0,k2=0;
        for (int i=0;i<scale/down_Kernel_Scale;i++)
            for (int j=0;j<scale/down_Kernel_Scale;j++){
                k1=i*down_Kernel_Scale+max_Refer[i][j] / 2;
                k2=j*down_Kernel_Scale+max_Refer[i][j] % 2;
                layer_Adjust[num][k1][k2]+=down_Layer_Adjust[i][j];
            }
    }
    //test pass
    public void set_Kernel_Adjust(int num,double[][] upper_Layer){
        double t=0;
        for (int i=0;i<kernel_Scale;i++)
            for (int j=0;j<kernel_Scale;j++){
                for (int k1=0;k1<scale;k1++)
                    for (int k2=0;k2<scale;k2++){
                        t= drelu(feature_Map[num][k1][k2])*upper_Layer[i+k1][j+k2]*layer_Adjust[num][k1][k2];
                        kernel_Adjust[num][i][j]+=t;
                        sum_Kernel_Adjust[num][i][j]+=t;
                    }
            }
    }
    //test pass
    public void set_Bias_Adjust(int num){
        double t=0;
        for (int i=0;i<scale;i++)
            for (int j=0;j<scale;j++){
                t=drelu(feature_Map[num][i][j])*layer_Adjust[num][i][j];
                bias_Adjust[num]+=t;
                sum_Bias_Adjust[num]+=t;
            }
    }
    //test pass
    public void renew_Adjust(double rate){
        for (int i=0;i<number;i++)
            for (int j=0;j<kernel_Scale;j++)
                for (int k=0;k<kernel_Scale;k++){
                    kernel_Map[i][j][k]+=rate*sum_Kernel_Adjust[i][j][k];
                    sum_Kernel_Adjust[i][j][k]=0;
                }
        for (int i=0;i<number;i++){
            bias[i]+=rate*sum_Bias_Adjust[i];
            sum_Bias_Adjust[i]=0;
        }
    }
    public void set_Kernel(int num,double[][] kernel){
        for (int i=0;i<kernel_Scale;i++)
            for (int j=0;j<kernel_Scale;j++){
                kernel_Map[num][i][j]=kernel[i][j];
            }
    }
    public void set_Bias(int num,double bias){
        this.bias[num]=bias;
    }

    public double get_Bias(int num){
        return bias[num];
    }
    public double[][] get_Kernel(int num){
        return kernel_Map[num];
    }
    public double[][] get_Layer(int num){
        return feature_Map[num];
    }
    public double[][] get_Layer_Adjust(int num){
        return layer_Adjust[num];
    }
    private double drelu(double x){
        if (x<=0) return 0;
        return 1;
    }
    private double relu(double x){
        if (x<=0) return 0;
        return x;
    }
}
