

public class AI extends Player {
    private int maxDepth;


    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public void move(BoardState state) {
        move = this.alphabetaSearch(state,this.maxDepth);
    }

    public int alphabetaSearch(BoardState state, int maxDepth) {
        int bestMove=0;
        int alpha= Integer.MIN_VALUE;
        int beta= Integer.MAX_VALUE;
        int v= Integer.MIN_VALUE;
        for(int i=0;i<6;i++) {
            if(state.isLegalMove(1, i)) {
                v= Math.max(v, minValue(state.applyMove(1, i),maxDepth,maxDepth-1,alpha,beta));
                if(v>alpha) {
                    alpha=v;
                    bestMove=i;
                }
                if(beta<=alpha)
                    break;
            }
        }
        return bestMove;
    }

    public int maxValue(BoardState state, int maxDepth, int currentDepth, int alpha, int beta) {
        if(currentDepth==0)
            return sbe(state);
        else {
            int v=Integer.MIN_VALUE;
            for(int i=0;i<6;i++) {
                if(state.isLegalMove(1, i)) {
                    v= Math.max(v, minValue(state.applyMove(1, i),maxDepth,currentDepth-1,alpha,beta));
                    if(v>=beta) {
                        break;
                    }
                    v=Math.max(alpha, v);
                } 
            }
            if(v==Integer.MIN_VALUE)
                return sbe(state);
            return v;
        }
    }

    public int minValue(BoardState state, int maxDepth, int currentDepth, int alpha, int beta) {
        if(currentDepth==0 )
            return sbe(state);
        else {
            int v=Integer.MAX_VALUE;
            for(int i=0;i<6;i++) {
                if(state.isLegalMove(2, i)) {
                    v= Math.min(v, maxValue(state.applyMove(2, i),maxDepth,currentDepth-1,alpha,beta));
                    if(v<=alpha) {
                        break;
                    }
                    beta=Math.min(beta,v);          
                }
            }
            if(v==Integer.MAX_VALUE)
                return sbe(state);
            return v;
        }
    }

    public int sbe(BoardState state){ //State board evaluation function used for Mini-Max Algorithm.
        return state.getMyScore(1)-state.getMyScore(2);
    }
    


}

