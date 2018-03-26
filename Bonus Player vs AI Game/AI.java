

/**
 * AI implemented using standard Alpha Beta Search(Mini-Max with pruning).
 * @author Shubham
 *
 */
public class AI extends Player {
    private int maxDepth;

    /**
     * Depth limit of mini-max algorthm. Higher the depth limit, more time the AI will take to 
     * play it's next move. Recommended value 15. For a higher value, change the THINK_TIME in match class
     * @param maxDepth the maximum depth till Mini-Max is computed
     */
    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    /**
     * move variable is assigned the best move deemed by the Mini-max search.
     */
    public void move(BoardState state) {
        move = this.alphabetaSearch(state,this.maxDepth);
    }

    /**
     * 
     * @param state the current state of the board
     * @param maxDepth the max depth till alphabeta (minimax) computes the best move
     * @return the best move for the AI to take
     */
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

    /**
     * Takes the maximum state board evaluation function value from the available moves.
     * @param state the current state of the board
     * @param maxDepth the maximum depth till mini-max is computed
     * @param currentDepth the current depth in the recursive function
     * @param alpha the alpha value of the move
     * @param beta the beta value of the move
     * @return the maximum value computed
     */
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

    /**
     * Takes the minimum state board evaluation function value from the available moves.
     * @param state the current state of the board
     * @param maxDepth the maximum depth till mini-max is computed
     * @param currentDepth the current depth in the recursive function
     * @param alpha the alpha value of the move
     * @param beta the beta value of the move
     * @return the minimum value computed
     */
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

