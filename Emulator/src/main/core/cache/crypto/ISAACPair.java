package core.cache.crypto;

/**
 * The type Isaac pair.
 */
public final class ISAACPair {

    private final ISAACCipher input;

    private final ISAACCipher output;

    /**
     * Instantiates a new Isaac pair.
     *
     * @param input  the input
     * @param output the output
     */
    public ISAACPair(ISAACCipher input, ISAACCipher output) {
        this.input = input;
        this.output = output;
    }

    /**
     * Gets input.
     *
     * @return the input
     */
    public ISAACCipher getInput() {
        return input;
    }

    /**
     * Gets output.
     *
     * @return the output
     */
    public ISAACCipher getOutput() {
        return output;
    }

}
