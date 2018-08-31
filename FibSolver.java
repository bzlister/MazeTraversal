import java.util.HashMap;

public class FibSolver{

    private HashMap<Integer, Long> f = new HashMap();
    private final int MODULO = 1000000007;
    
    public int solve(int a, int b, int n) {
        return (int)((solver(n)*b + solver(n-1)*a)%MODULO);
    }
    
    private long solver(int n){
        long retVal;
        if (n == 0)
            retVal = 0;
        else if (n == 1)
            retVal =  1;
        else{
            int h = (int)Math.ceil(n/2.0);
            if (!f.containsKey(h))
                f.put(h, solver(h));
            if (!f.containsKey(h-1))
                f.put(h-1, solver(h-1));
            long fB = f.get(h);
            long fA = f.get(h-1);
            if (n%2 == 0)
                retVal = fB*(2*fA + fB);
            else{
                retVal = fB*fB + fA*fA;
            }
        }
        return retVal%MODULO;
    }
}