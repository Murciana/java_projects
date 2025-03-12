import javax.sound.sampled.*;
import java.util.HashMap;

public class MorseCodeController {
    //will a hashmap  to translate an input into morse code ( letter (key), morse (value)

    private HashMap<Character, String> morseCodeMap;

    public MorseCodeController(){
        morseCodeMap = new HashMap<>();

        // uppercase
        morseCodeMap.put('A', ".-");
        morseCodeMap.put('B', "-...");
        morseCodeMap.put('C', "-.-.");
        morseCodeMap.put('D', "-..");
        morseCodeMap.put('E', ".");
        morseCodeMap.put('F', "..-.");
        morseCodeMap.put('G', "--.");
        morseCodeMap.put('H', "....");
        morseCodeMap.put('I', "..");
        morseCodeMap.put('J', ".---");
        morseCodeMap.put('K', "-.-");
        morseCodeMap.put('L', ".-..");
        morseCodeMap.put('M', "--");
        morseCodeMap.put('N', "-.");
        morseCodeMap.put('O', "---");
        morseCodeMap.put('P', ".--.");
        morseCodeMap.put('Q', "--.-");
        morseCodeMap.put('R', ".-.");
        morseCodeMap.put('S', "...");
        morseCodeMap.put('T', "-");
        morseCodeMap.put('U', "..-");
        morseCodeMap.put('V', "...-");
        morseCodeMap.put('W', ".--");
        morseCodeMap.put('X', "-..-");
        morseCodeMap.put('Y', "-.--");
        morseCodeMap.put('Z', "--..");

        // lowercase
        morseCodeMap.put('a', ".-");
        morseCodeMap.put('b', "-...");
        morseCodeMap.put('c', "-.-.");
        morseCodeMap.put('d', "-..");
        morseCodeMap.put('e', ".");
        morseCodeMap.put('f', "..-.");
        morseCodeMap.put('g', "--.");
        morseCodeMap.put('h', "....");
        morseCodeMap.put('i', "..");
        morseCodeMap.put('j', ".---");
        morseCodeMap.put('k', "-.-");
        morseCodeMap.put('l', ".-..");
        morseCodeMap.put('m', "--");
        morseCodeMap.put('n', "-.");
        morseCodeMap.put('o', "---");
        morseCodeMap.put('p', ".--.");
        morseCodeMap.put('q', "--.-");
        morseCodeMap.put('r', ".-.");
        morseCodeMap.put('s', "...");
        morseCodeMap.put('t', "-");
        morseCodeMap.put('u', "..-");
        morseCodeMap.put('v', "...-");
        morseCodeMap.put('w', ".--");
        morseCodeMap.put('x', "-..-");
        morseCodeMap.put('y', "-.--");
        morseCodeMap.put('z', "--..");

        // numbers
        morseCodeMap.put('0', "-----");
        morseCodeMap.put('1', ".----");
        morseCodeMap.put('2', "..---");
        morseCodeMap.put('3', "...--");
        morseCodeMap.put('4', "....-");
        morseCodeMap.put('5', ".....");
        morseCodeMap.put('6', "-....");
        morseCodeMap.put('7', "--...");
        morseCodeMap.put('8', "---..");
        morseCodeMap.put('9', "----.");

        // special characters
        morseCodeMap.put(' ', "/");
        morseCodeMap.put(',', "--..--");
        morseCodeMap.put('.', ".-.-.-");
        morseCodeMap.put('?', "..--..");
        morseCodeMap.put(';', "-.-.-.");
        morseCodeMap.put(':', "---...");
        morseCodeMap.put('(', "-.--.");
        morseCodeMap.put(')', "-.--.-");
        morseCodeMap.put('[', "-.--.");
        morseCodeMap.put(']', "-.--.-");
        morseCodeMap.put('{', "-.--.");
        morseCodeMap.put('}', "-.--.-");
        morseCodeMap.put('+', ".-.-.");
        morseCodeMap.put('-', "-....-");
        morseCodeMap.put('_', "..--.-");
        morseCodeMap.put('"', ".-..-.");
        morseCodeMap.put('\'', ".----.");
        morseCodeMap.put('/', "-..-.");
        morseCodeMap.put('\\', "-..-.");
        morseCodeMap.put('@', ".--.-.");
        morseCodeMap.put('=', "-...-");
        morseCodeMap.put('!', "-.-.--");

    }

    public String translateToMorse(String textToTranslate) {
        StringBuilder translatedText = new StringBuilder();
        for(Character letter : textToTranslate.toCharArray()){
            translatedText.append(morseCodeMap.get(letter) + " ");
        }
        return translatedText.toString();
    }

    public void playSound(String[] morseMessage) throws LineUnavailableException, InterruptedException {
        AudioFormat audioFormat = new AudioFormat(44100, 16,1, true, false);

        //create the data line to play incoming audio data
        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
        SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
        sourceDataLine.open(audioFormat);
        sourceDataLine.start();

        //duration of the sound
        int dotDuration = 200;
        int dashDuration = (int) (1.5 * dotDuration);
        int slashDuration = 2 * dashDuration ;

        for (String patter : morseMessage) {
            //play the letter sound
            for (char c : patter.toCharArray()){
                if(c == '.' ){
                    playBeep(sourceDataLine, dotDuration);
                    Thread.sleep(dotDuration);
                } else if (c == '-') {
                    playBeep(sourceDataLine, dashDuration);
                    Thread.sleep(dotDuration);
                } else if (c == '/') {
                    Thread.sleep(slashDuration);
                }
            }
            // waits a bit before playing the next sequence
            Thread.sleep(dotDuration);
        }
        // close audio output line (cleans up resource)

        sourceDataLine.drain();
        sourceDataLine.stop();
        sourceDataLine.close();
    }

    private void playBeep(SourceDataLine line, int duration) {
        //create audio data
        byte[] data = new byte[duration * 44100 / 1000];

        for (int i = 0; i < data.length ; i++) {
            //calculate the angle of the sine wave for the current sample based on the sample frequency
            double angle = i / (44100.8/440) * 2 * Math.PI;
            //calculate the amplitude of the sine wave at the current angle
            data[i] = (byte) (Math.sin(angle) * 127) ;
        }
        //write the audio dot in the data line to be played
        line.write(data, 0, data.length);
    }
}

