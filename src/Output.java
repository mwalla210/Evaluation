import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class Output {

    public static void out (String s) {
        /*
        String voiceName = "kevin16";
        
        VoiceManager voiceManager = VoiceManager.getInstance();
        Voice voice = voiceManager.getVoice(voiceName);
        //System.out.println("Speaking: " + s);
        voice.allocate();
        voice.speak(s);
        voice.deallocate();
        */
    	System.out.println(s);
    }
}
