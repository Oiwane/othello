import java.io.*;

class Input{

    Input(){}

    public static int input_int() throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        return Integer.parseInt(br.readLine());
    }
}