// Generated from /user/1/lennea/Projet_GL/src/main/antlr4/fr/ensimag/deca/syntax/DecaLexer.g4 by ANTLR 4.9.2
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class DecaLexer extends AbstractDecaLexer {
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		LETTER=1, DIGIT=2, EOL=3, IDENT=4, POSITIVE_DIGIT=5, INT=6, NUM=7, SIGN=8, 
		OBRACE=9, CBRACE=10, CPARENT=11, OPARENT=12, SEMI=13, PRINTLN=14, PRINT=15, 
		SPACES=16, DUMMY_TOKEN=17;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"LETTER", "DIGIT", "EOL", "IDENT", "POSITIVE_DIGIT", "INT", "NUM", "SIGN", 
			"OBRACE", "CBRACE", "CPARENT", "OPARENT", "SEMI", "PRINTLN", "PRINT", 
			"SPACES", "DUMMY_TOKEN"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, "'\n'", null, null, null, null, null, "'{'", "'}'", 
			"')'", "'('", "';'", "'println'", "'print'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "LETTER", "DIGIT", "EOL", "IDENT", "POSITIVE_DIGIT", "INT", "NUM", 
			"SIGN", "OBRACE", "CBRACE", "CPARENT", "OPARENT", "SEMI", "PRINTLN", 
			"PRINT", "SPACES", "DUMMY_TOKEN"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}




	public DecaLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "DecaLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 15:
			SPACES_action((RuleContext)_localctx, actionIndex);
			break;
		}
	}
	private void SPACES_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:
			skip(); 
			break;
		}
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\23|\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\3\2\3\2\3\3\3\3\3\4\3\4\3\5\6\5-\n\5\r\5\16\5.\3\5\6\5\62\n\5\r\5\16"+
		"\5\63\3\5\3\5\3\5\6\59\n\5\r\5\16\5:\3\5\6\5>\n\5\r\5\16\5?\3\5\6\5C\n"+
		"\5\r\5\16\5D\3\5\3\5\7\5I\n\5\f\5\16\5L\13\5\3\6\3\6\3\7\3\7\7\7R\n\7"+
		"\f\7\16\7U\13\7\3\b\6\bX\n\b\r\b\16\bY\3\t\3\t\5\t^\n\t\3\n\3\n\3\13\3"+
		"\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17"+
		"\3\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\22\3\22\2\2\23\3\3\5\4"+
		"\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22"+
		"#\23\3\2\7\4\2C\\c|\3\2\62;\3\2\63;\4\2--//\4\2\f\f\"\"\2\u0084\2\3\3"+
		"\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2"+
		"\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3"+
		"\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\3"+
		"%\3\2\2\2\5\'\3\2\2\2\7)\3\2\2\2\t,\3\2\2\2\13M\3\2\2\2\rO\3\2\2\2\17"+
		"W\3\2\2\2\21]\3\2\2\2\23_\3\2\2\2\25a\3\2\2\2\27c\3\2\2\2\31e\3\2\2\2"+
		"\33g\3\2\2\2\35i\3\2\2\2\37q\3\2\2\2!w\3\2\2\2#z\3\2\2\2%&\t\2\2\2&\4"+
		"\3\2\2\2\'(\t\3\2\2(\6\3\2\2\2)*\7\f\2\2*\b\3\2\2\2+-\5\3\2\2,+\3\2\2"+
		"\2-.\3\2\2\2.,\3\2\2\2./\3\2\2\2/\61\3\2\2\2\60\62\7&\2\2\61\60\3\2\2"+
		"\2\62\63\3\2\2\2\63\61\3\2\2\2\63\64\3\2\2\2\64\65\3\2\2\2\65\66\7a\2"+
		"\2\66J\3\2\2\2\679\5\3\2\28\67\3\2\2\29:\3\2\2\2:8\3\2\2\2:;\3\2\2\2;"+
		"=\3\2\2\2<>\5\5\3\2=<\3\2\2\2>?\3\2\2\2?=\3\2\2\2?@\3\2\2\2@B\3\2\2\2"+
		"AC\7&\2\2BA\3\2\2\2CD\3\2\2\2DB\3\2\2\2DE\3\2\2\2EF\3\2\2\2FG\7a\2\2G"+
		"I\3\2\2\2H8\3\2\2\2IL\3\2\2\2JH\3\2\2\2JK\3\2\2\2K\n\3\2\2\2LJ\3\2\2\2"+
		"MN\t\4\2\2N\f\3\2\2\2OS\7\62\2\2PR\5\13\6\2QP\3\2\2\2RU\3\2\2\2SQ\3\2"+
		"\2\2ST\3\2\2\2T\16\3\2\2\2US\3\2\2\2VX\5\5\3\2WV\3\2\2\2XY\3\2\2\2YW\3"+
		"\2\2\2YZ\3\2\2\2Z\20\3\2\2\2[^\t\5\2\2\\^\3\2\2\2][\3\2\2\2]\\\3\2\2\2"+
		"^\22\3\2\2\2_`\7}\2\2`\24\3\2\2\2ab\7\177\2\2b\26\3\2\2\2cd\7+\2\2d\30"+
		"\3\2\2\2ef\7*\2\2f\32\3\2\2\2gh\7=\2\2h\34\3\2\2\2ij\7r\2\2jk\7t\2\2k"+
		"l\7k\2\2lm\7p\2\2mn\7v\2\2no\7n\2\2op\7p\2\2p\36\3\2\2\2qr\7r\2\2rs\7"+
		"t\2\2st\7k\2\2tu\7p\2\2uv\7v\2\2v \3\2\2\2wx\t\6\2\2xy\b\21\2\2y\"\3\2"+
		"\2\2z{\13\2\2\2{$\3\2\2\2\f\2.\63:?DJSY]\3\3\21\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}