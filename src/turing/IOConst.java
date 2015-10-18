package turing;

/**
 * @author jose
 *
 *  Collection of constants.
 *  Models the arbitrary positions of data
 *  that were chosen as the input/output standard.
 *  It helps give semantics to the base string data.
 *  
 *  These will only be used by the TuringIO class.
 */
public abstract class IOConst {
    
    /*
     * Input constants.
     */
    
    // Data positions in processed file.
    final static int IN_FILE_STATE_SET = 0;
    final static int IN_FILE_INPUT_ALPH = 1;
    final static int IN_FILE_OUTPUT_ALPH = 2;
    final static int IN_FILE_INIT_STATE = 3;
    final static int IN_FILE_BLANK_CHAR = 4;
    final static int IN_FILE_ACCEPT_STATES = 5;
    final static int IN_FILE_TRANS_FUNCT = 6; // Where the transition lines BEGIN.
    
    // Data positions in each processed transition line. 
    final static int IN_TRAN_PREV_STATE = 0;
    final static int IN_TRAN_NEXT_STATE = 2;
    final static int IN_TRAN_INPUT_CHAR = 1;
    final static int IN_TRAN_OUTPUT_CHAR = 3;
    final static int IN_TRAN_MOVEMENT = 4;
    
    /*
     * Output constants.
     */
    
    // Data positions in each transition line to output.
    final static int OUT_TRAN_PREV_STATE = 0;
    final static int OUT_TRAN_NEXT_STATE = 2;
    final static int OUT_TRAN_INPUT_CHAR = 1;
    final static int OUT_TRAN_OUTPUT_CHAR = 3;
    final static int OUT_TRAN_MOVEMENT = 4;
    
    // Token positions in each Turing machine status to output.
    final static int OUT_STAT_CUR_STATE = 0;
    final static int OUT_STAT_TAPE = 1;
    final static int OUT_STAT_HEAD_POS = 2;
}
