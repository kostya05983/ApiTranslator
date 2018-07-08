import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import util.YandexTranslator;

@RunWith(JUnit4.class)
public class YandexTranslatorTest extends Assert {

    @Test
    public void nullLang() {
        try(YandexTranslator translator = new YandexTranslator()) {
            assertNull(translator.translate("cat", null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void nullWord() {
        try(YandexTranslator translator = new YandexTranslator()) {
            assertNull(translator.translate(null,"ru"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void nullParams() {
        try(YandexTranslator translator = new YandexTranslator()) {
            assertNull(translator.translate(null,null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void unknownLang() {
     try(YandexTranslator translator = new YandexTranslator()) {
         translator.translate("word","bla");
     } catch (Exception e) {
         e.printStackTrace();
     }
    }

    @Test
    public void unknownWord() {
        try(YandexTranslator translator = new YandexTranslator()) {
            translator.translate("l;;sdf","ru");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void translate() {
        try(YandexTranslator translator = new YandexTranslator()) {
            assertEquals(translator.translate("Word","ru"),"Слово");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
