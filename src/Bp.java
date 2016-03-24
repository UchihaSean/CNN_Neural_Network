/**
 * Created by Jly_wave on 10/22/15.
 */
public class Bp {
    private int number,upper_Number;
    private double[] hidden,bias,layer_Adjust,bias_Adjust,sum_Bias_Adjust;
    private double[][] weight,weight_Adjust,sum_Weight_Adjust;
    public Bp(int number,int upper_Number){
        hidden=new double[number];
        weight=new double[upper_Number][number];
        weight_Adjust=new double[upper_Number][number];
        sum_Weight_Adjust=new double[upper_Number][number];
        layer_Adjust=new double[number];
        bias_Adjust=new double[number];
        sum_Bias_Adjust=new double[number];
        bias=new double[number];
        this.number=number;
        this.upper_Number=upper_Number;
        for (int i=0;i<upper_Number;i++) {
            //test
//            weight[i][0]=-1.0/2;
            for (int j = 0; j < number; j++) {
                weight[i][j] = (Math.random() * 2 - 1) / Math.sqrt(upper_Number);
                //test
//                weight[i][j]=-weight[i][j-1];
                //test
            }
        }
        //test
//        bias[0]=-1;
        for (int i=0;i<number;i++) {
            bias[i] = -1 + 2 * Math.random();
            //test
//            bias[i]=-bias[i-1];
            //
        }

        for (int i=0;i<upper_Number;i++)
            for (int j=0;j<number;j++) {
                weight_Adjust[i][j] = 0;
                sum_Weight_Adjust[i][j]=0;
            }
        for (int i=0;i<number;i++) {
            sum_Bias_Adjust[i]=0;
            bias_Adjust[i] = 0;
            layer_Adjust[i]=0;
        }
        initial();
    }
    public void initial(){
        for (int i=0;i<upper_Number;i++)
            for (int j=0;j<number;j++) {
                weight_Adjust[i][j] = 0;
            }
        for (int i=0;i<number;i++){
            bias_Adjust[i]=0;
            layer_Adjust[i]=0;
            hidden[i]=0;
        }
    }
    public void set_Layer(int num,double upper_Layer,int upper_Num){
        hidden[num]+=weight[upper_Num][num]*upper_Layer;
    }

    public void addBiasRelu(){
        for (int i=0;i<number;i++)
            hidden[i]=relu(hidden[i] + bias[i]);
    }
    public void addBias(){
        for (int i=0;i<number;i++)
            hidden[i]=hidden[i] + bias[i];
    }
    public void set_Layer_Adjust(int[] standard){
        for (int i=0;i<number;i++){
            layer_Adjust[i]=standard[i]-hidden[i];
        }
    }
    public void set_Weight_Adjust(int upper,int now,double upper_Layer){
        double t=drelu(hidden[now])*layer_Adjust[now]*upper_Layer;
        weight_Adjust[upper][now]+=t;
        sum_Weight_Adjust[upper][now]+=t;
    }
    public void set_Bias_Adjust(int now){
        double t=drelu(hidden[now])*layer_Adjust[now];
        bias_Adjust[now]+=t;
        sum_Bias_Adjust[now]+=t;
    }
    public void set_Layer_Adjust(int now,double down_Layer_Adjust,double down_Layer,double down_Weight){
        layer_Adjust[now]+=down_Layer_Adjust*drelu(down_Layer)*down_Weight;
    }
    public void softMax(){
        double sum=0,max=-1e20;
        for (int i=0;i<number;i++){
            if (max<hidden[i])
                max=hidden[i];
        }
        for (int i=0;i<number;i++){
            hidden[i]=Math.exp(hidden[i]-max);
            sum+=hidden[i];
        }
        for (int i=0;i<number;i++){
            hidden[i]/=sum;
        }
    }
    public void renew_Adjust(double rate){
        for (int i=0;i<upper_Number;i++)
            for (int j=0;j<number;j++) {
                weight[i][j]+=rate*sum_Weight_Adjust[i][j];
                sum_Weight_Adjust[i][j] = 0;
            }
        for (int i=0;i<number;i++){
            bias[i]+=rate*sum_Bias_Adjust[i];
            sum_Bias_Adjust[i]=0;
        }
    }
    public void set_Weight(int upper,int now,double weight){
        this.weight[upper][now]=weight;
    }
    public void set_Bias(int num,double bias){
        this.bias[num]=bias;
    }


    public double get_Bias(int num){
        return bias[num];
    }
    public double get_Layer_Adjust(int num){
        return layer_Adjust[num];
    }
    public double get_Weight(int i,int j){
        return weight[i][j];
    }
    public double get_Layer(int num){
        return hidden[num];
    }
    private double relu(double x){
        if (x<0) return 0;
        return x;
    }
    private double drelu(double x){
        if (x<=0) return 0;
        return 1;
    }
}
