// Generated from /user/9/.base/soriarag/home/Projet_GL/src/main/antlr4/fr/ensimag/deca/syntax/DecaLexer.g4 by ANTLR 4.9.2
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
		PRINTLN=1, PRINT=2, EOL=3, SPACES=4, IDENT=5, INT=6, FLOAT=7, OPARENT=8, 
		CPARENT=9, OBRACE=10, CBRACE=11, SEMI=12, STRING=13, MULTI_LINE_STRING=14, 
		COMMENT=15, MULTI_LINE_COMMENT=16, DUMMY_TOKEN=17;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"PRINTLN", "PRINT", "LETTER", "DIGIT", "EOL", "SPACES", "IDENT", "POSITIVE_DIGIT", 
			"INT", "NUM", "SIGN", "EXP", "DEC", "FLOATDEC", "DIGITHEX", "NUMHEX", 
			"FLOATHEX", "FLOAT", "OPARENT", "CPARENT", "OBRACE", "CBRACE", "SEMI", 
			"STRING_CAR", "STRING", "MULTI_LINE_STRING", "COMMENT", "MULTI_LINE_COMMENT", 
			"DUMMY_TOKEN"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'println'", "'print'", "'\n'", "' '", null, null, null, "'('", 
			"')'", "'{'", "'}'", "';'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "PRINTLN", "PRINT", "EOL", "SPACES", "IDENT", "INT", "FLOAT", "OPARENT", 
			"CPARENT", "OBRACE", "CBRACE", "SEMI", "STRING", "MULTI_LINE_STRING", 
			"COMMENT", "MULTI_LINE_COMMENT", "DUMMY_TOKEN"
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
		case 5:
			SPACES_action((RuleContext)_localctx, actionIndex);
			break;
		case 26:
			COMMENT_action((RuleContext)_localctx, actionIndex);
			break;
		case 27:
			MULTI_LINE_COMMENT_action((RuleContext)_localctx, actionIndex);
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
	private void COMMENT_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 1:
			 skip(); 
			break;
		}
	}
	private void MULTI_LINE_COMMENT_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 2:
			 skip(); 
			break;
		}
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\23\u00e5\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\3\2\3\2\3\2\3"+
		"\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7"+
		"\3\7\3\7\3\b\3\b\5\bW\n\b\3\b\3\b\3\b\7\b\\\n\b\f\b\16\b_\13\b\3\t\3\t"+
		"\3\n\3\n\3\n\7\nf\n\n\f\n\16\ni\13\n\5\nk\n\n\3\13\6\13n\n\13\r\13\16"+
		"\13o\3\f\3\f\5\ft\n\f\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\17\3\17\3"+
		"\17\3\17\5\17\u0082\n\17\3\17\3\17\5\17\u0086\n\17\3\20\3\20\3\21\6\21"+
		"\u008b\n\21\r\21\16\21\u008c\3\22\3\22\3\22\3\22\5\22\u0093\n\22\3\22"+
		"\3\22\3\22\3\22\3\22\3\22\3\22\3\22\5\22\u009d\n\22\3\23\3\23\5\23\u00a1"+
		"\n\23\3\24\3\24\3\25\3\25\3\26\3\26\3\27\3\27\3\30\3\30\3\31\3\31\3\32"+
		"\3\32\3\32\3\32\3\32\3\32\7\32\u00b5\n\32\f\32\16\32\u00b8\13\32\3\32"+
		"\3\32\3\33\3\33\3\33\3\33\3\33\3\33\3\33\7\33\u00c3\n\33\f\33\16\33\u00c6"+
		"\13\33\3\33\3\33\3\34\3\34\3\34\3\34\7\34\u00ce\n\34\f\34\16\34\u00d1"+
		"\13\34\3\34\3\34\3\35\3\35\3\35\3\35\3\35\7\35\u00da\n\35\f\35\16\35\u00dd"+
		"\13\35\3\35\3\35\3\35\3\35\3\35\3\36\3\36\2\2\37\3\3\5\4\7\2\t\2\13\5"+
		"\r\6\17\7\21\2\23\b\25\2\27\2\31\2\33\2\35\2\37\2!\2#\2%\t\'\n)\13+\f"+
		"-\r/\16\61\2\63\17\65\20\67\219\22;\23\3\2\n\4\2C\\c|\4\2&&aa\4\2--//"+
		"\4\2GGgg\4\2HHhh\5\2\62;CHch\4\2RRrr\3\2\f\f\2\u00f0\2\3\3\2\2\2\2\5\3"+
		"\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\23\3\2\2\2\2%\3\2\2\2\2"+
		"\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\63\3\2\2\2\2"+
		"\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\3=\3\2\2\2\5E\3\2\2\2\7"+
		"K\3\2\2\2\tM\3\2\2\2\13O\3\2\2\2\rQ\3\2\2\2\17V\3\2\2\2\21`\3\2\2\2\23"+
		"j\3\2\2\2\25m\3\2\2\2\27s\3\2\2\2\31u\3\2\2\2\33y\3\2\2\2\35\u0081\3\2"+
		"\2\2\37\u0087\3\2\2\2!\u008a\3\2\2\2#\u0092\3\2\2\2%\u00a0\3\2\2\2\'\u00a2"+
		"\3\2\2\2)\u00a4\3\2\2\2+\u00a6\3\2\2\2-\u00a8\3\2\2\2/\u00aa\3\2\2\2\61"+
		"\u00ac\3\2\2\2\63\u00ae\3\2\2\2\65\u00bb\3\2\2\2\67\u00c9\3\2\2\29\u00d4"+
		"\3\2\2\2;\u00e3\3\2\2\2=>\7r\2\2>?\7t\2\2?@\7k\2\2@A\7p\2\2AB\7v\2\2B"+
		"C\7n\2\2CD\7p\2\2D\4\3\2\2\2EF\7r\2\2FG\7t\2\2GH\7k\2\2HI\7p\2\2IJ\7v"+
		"\2\2J\6\3\2\2\2KL\t\2\2\2L\b\3\2\2\2MN\4\62;\2N\n\3\2\2\2OP\7\f\2\2P\f"+
		"\3\2\2\2QR\7\"\2\2RS\b\7\2\2S\16\3\2\2\2TW\5\7\4\2UW\t\3\2\2VT\3\2\2\2"+
		"VU\3\2\2\2W]\3\2\2\2X\\\5\7\4\2Y\\\5\t\5\2Z\\\t\3\2\2[X\3\2\2\2[Y\3\2"+
		"\2\2[Z\3\2\2\2\\_\3\2\2\2][\3\2\2\2]^\3\2\2\2^\20\3\2\2\2_]\3\2\2\2`a"+
		"\4\63;\2a\22\3\2\2\2bk\7\62\2\2cg\5\21\t\2df\5\t\5\2ed\3\2\2\2fi\3\2\2"+
		"\2ge\3\2\2\2gh\3\2\2\2hk\3\2\2\2ig\3\2\2\2jb\3\2\2\2jc\3\2\2\2k\24\3\2"+
		"\2\2ln\5\t\5\2ml\3\2\2\2no\3\2\2\2om\3\2\2\2op\3\2\2\2p\26\3\2\2\2qt\t"+
		"\4\2\2rt\3\2\2\2sq\3\2\2\2sr\3\2\2\2t\30\3\2\2\2uv\t\5\2\2vw\5\27\f\2"+
		"wx\5\25\13\2x\32\3\2\2\2yz\5\25\13\2z{\7\60\2\2{|\5\25\13\2|\34\3\2\2"+
		"\2}\u0082\5\33\16\2~\177\5\33\16\2\177\u0080\5\31\r\2\u0080\u0082\3\2"+
		"\2\2\u0081}\3\2\2\2\u0081~\3\2\2\2\u0082\u0085\3\2\2\2\u0083\u0086\t\6"+
		"\2\2\u0084\u0086\3\2\2\2\u0085\u0083\3\2\2\2\u0085\u0084\3\2\2\2\u0086"+
		"\36\3\2\2\2\u0087\u0088\t\7\2\2\u0088 \3\2\2\2\u0089\u008b\5\37\20\2\u008a"+
		"\u0089\3\2\2\2\u008b\u008c\3\2\2\2\u008c\u008a\3\2\2\2\u008c\u008d\3\2"+
		"\2\2\u008d\"\3\2\2\2\u008e\u008f\7\62\2\2\u008f\u0093\7z\2\2\u0090\u0091"+
		"\7\62\2\2\u0091\u0093\7Z\2\2\u0092\u008e\3\2\2\2\u0092\u0090\3\2\2\2\u0093"+
		"\u0094\3\2\2\2\u0094\u0095\5!\21\2\u0095\u0096\7\60\2\2\u0096\u0097\5"+
		"!\21\2\u0097\u0098\t\b\2\2\u0098\u0099\5\27\f\2\u0099\u009c\5\25\13\2"+
		"\u009a\u009d\t\6\2\2\u009b\u009d\3\2\2\2\u009c\u009a\3\2\2\2\u009c\u009b"+
		"\3\2\2\2\u009d$\3\2\2\2\u009e\u00a1\5\35\17\2\u009f\u00a1\5#\22\2\u00a0"+
		"\u009e\3\2\2\2\u00a0\u009f\3\2\2\2\u00a1&\3\2\2\2\u00a2\u00a3\7*\2\2\u00a3"+
		"(\3\2\2\2\u00a4\u00a5\7+\2\2\u00a5*\3\2\2\2\u00a6\u00a7\7}\2\2\u00a7,"+
		"\3\2\2\2\u00a8\u00a9\7\177\2\2\u00a9.\3\2\2\2\u00aa\u00ab\7=\2\2\u00ab"+
		"\60\3\2\2\2\u00ac\u00ad\13\2\2\2\u00ad\62\3\2\2\2\u00ae\u00b6\7$\2\2\u00af"+
		"\u00b5\5\61\31\2\u00b0\u00b1\7^\2\2\u00b1\u00b5\7$\2\2\u00b2\u00b3\7^"+
		"\2\2\u00b3\u00b5\7^\2\2\u00b4\u00af\3\2\2\2\u00b4\u00b0\3\2\2\2\u00b4"+
		"\u00b2\3\2\2\2\u00b5\u00b8\3\2\2\2\u00b6\u00b4\3\2\2\2\u00b6\u00b7\3\2"+
		"\2\2\u00b7\u00b9\3\2\2\2\u00b8\u00b6\3\2\2\2\u00b9\u00ba\7$\2\2\u00ba"+
		"\64\3\2\2\2\u00bb\u00c4\7$\2\2\u00bc\u00c3\5\61\31\2\u00bd\u00c3\5\13"+
		"\6\2\u00be\u00bf\7^\2\2\u00bf\u00c3\7$\2\2\u00c0\u00c1\7^\2\2\u00c1\u00c3"+
		"\7^\2\2\u00c2\u00bc\3\2\2\2\u00c2\u00bd\3\2\2\2\u00c2\u00be\3\2\2\2\u00c2"+
		"\u00c0\3\2\2\2\u00c3\u00c6\3\2\2\2\u00c4\u00c2\3\2\2\2\u00c4\u00c5\3\2"+
		"\2\2\u00c5\u00c7\3\2\2\2\u00c6\u00c4\3\2\2\2\u00c7\u00c8\7$\2\2\u00c8"+
		"\66\3\2\2\2\u00c9\u00ca\7\61\2\2\u00ca\u00cb\7\61\2\2\u00cb\u00cf\3\2"+
		"\2\2\u00cc\u00ce\n\t\2\2\u00cd\u00cc\3\2\2\2\u00ce\u00d1\3\2\2\2\u00cf"+
		"\u00cd\3\2\2\2\u00cf\u00d0\3\2\2\2\u00d0\u00d2\3\2\2\2\u00d1\u00cf\3\2"+
		"\2\2\u00d2\u00d3\b\34\3\2\u00d38\3\2\2\2\u00d4\u00d5\7\61\2\2\u00d5\u00d6"+
		"\7,\2\2\u00d6\u00db\3\2\2\2\u00d7\u00da\5\61\31\2\u00d8\u00da\5\13\6\2"+
		"\u00d9\u00d7\3\2\2\2\u00d9\u00d8\3\2\2\2\u00da\u00dd\3\2\2\2\u00db\u00d9"+
		"\3\2\2\2\u00db\u00dc\3\2\2\2\u00dc\u00de\3\2\2\2\u00dd\u00db\3\2\2\2\u00de"+
		"\u00df\7,\2\2\u00df\u00e0\7\61\2\2\u00e0\u00e1\3\2\2\2\u00e1\u00e2\b\35"+
		"\4\2\u00e2:\3\2\2\2\u00e3\u00e4\13\2\2\2\u00e4<\3\2\2\2\27\2V[]gjos\u0081"+
		"\u0085\u008c\u0092\u009c\u00a0\u00b4\u00b6\u00c2\u00c4\u00cf\u00d9\u00db"+
		"\5\3\7\2\3\34\3\3\35\4";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}