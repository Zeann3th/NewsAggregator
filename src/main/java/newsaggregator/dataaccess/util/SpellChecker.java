package newsaggregator.dataaccess.util;

import org.languagetool.JLanguageTool;
import org.languagetool.Languages;
import org.languagetool.rules.RuleMatch;

import java.io.IOException;
import java.util.List;

/**
 * Lớp này dùng để kiểm tra chính tả của một chuỗi.
 * <br> Sử dụng thư viện LanguageTool để kiểm tra chính tả.
 * @see <a href="https://languagetool.org/">LanguageTool</a>
 * @see <a href="https://languagetool.org/download/LanguageTool-5.5.zip">LanguageTool-5.5</a>
 * @since 1.0
 */
public class SpellChecker {
    //TODO: Find a way to change the incorrect string into the suggested string
    public void check(String s) throws IOException {
        JLanguageTool langTool = new JLanguageTool(Languages.getLanguageForShortCode("en-GB"));
        List<RuleMatch> matches = langTool.check(s);
        for (RuleMatch match : matches) {
            System.out.println("Potential error at characters " +
                    match.getFromPos() + "-" + match.getToPos() + ": " +
                    match.getMessage());
            System.out.println("Suggested correction(s): " +
                    match.getSuggestedReplacements());
        }
    }
}
