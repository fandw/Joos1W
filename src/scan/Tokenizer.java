package scan;

import common.Token;
import common.TokenType;
import exception.ScannerException;
import util.Location;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by daiweifan on 2017-04-28.
 */
public class Tokenizer {
    private static TransitionSet transitionSet = TransitionSet.getTransitionSet();

    public static List<Token> scan(String filePath) throws ScannerException, FileNotFoundException{
        List<Token> tokenList = new ArrayList<>();
        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(filePath));
        Scanner in = new Scanner(inputStream);
        int lineNumber = 0;
        boolean inComment = false;
        tokenList.add(new Token(TokenType.BOF, "bof", lineNumber, 0, filePath));

        while (in.hasNextLine()) {
            String line = in.nextLine();
            lineNumber++;

            if (line.length() == 0) continue;
            int charPointer = 0;
            int startIndex = 0;

            State state = inComment ? State.INCOMMENT : State.START;

            while (charPointer <= line.length()) {
                char currentChar = charPointer == line.length() ? '\n' : line.charAt(charPointer);
                State toState = transitionSet.findToState(state, currentChar);
                if (toState == null) {
                    // restore to in-comment state
                    if (state == State.RCOMMEN || state == State.INCOMMENT) {
                        inComment = true;
                        state = State.INCOMMENT;
                        charPointer++;
                        continue;
                    }

                    // one-line comment
                    if (state == State.SCOMMENT) {
                        break;
                    }

                    // state not final
                    if (state.getValidType() == null) {
                        throw new ScannerException(new Location(lineNumber, charPointer, filePath),
                                "Invalid Token " + line.substring(startIndex, charPointer));
                    }

                    // special literals or keywords
                    if (state.getValidType() == TokenType.ID) {
                        if (line.substring(startIndex, charPointer).equals("true") ||
                            line.substring(startIndex, charPointer).equals("false")) {
                            state = State.BooleanLiteral;
                        } else if (line.substring(startIndex, charPointer).equals("null")) {
                            state = State.NullLiteral;
                        } else if (Keyword.getKeywordSet().containsKey(line.substring(startIndex, charPointer))) {
                            state = State.KEYWORD;
                            state.setKeywordTokenType(line.substring(startIndex, charPointer));
                        }
                    }

                    // special tokens that are not recorded. ex: whitespaces, comments
                    if (state.getValidType() != TokenType.SPECIAL) {
                        tokenList.add(new Token(state.getValidType(), line.substring(startIndex, charPointer),
                                lineNumber, startIndex, filePath));
                    }

                    // end of comment
                    if (state == State.COMMENT) {
                        inComment = false;
                    }

                    startIndex = charPointer;
                    state = State.START;

                } else {
                    state = toState;
                    charPointer++;
                }
            }
        }

        tokenList.add(new Token(TokenType.EOF, "eof", lineNumber, 0, filePath));
        System.out.flush();
        return tokenList;
    }
}
