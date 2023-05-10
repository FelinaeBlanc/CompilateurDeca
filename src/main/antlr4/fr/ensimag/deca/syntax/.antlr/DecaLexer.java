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
		PRINTLN=1, PRINT=2, LETTER=3, DIGIT=4, EOL=5, IDENT=6, POSITIVE_DIGIT=7, 
		INT=8, OBRACE=9, CBRACE=10, SEMI=11, STRING=12, MULTI_LINE_STRING=13, 
		COMMENT=14, MULTI_LINE_COMMENT=15, DUMMY_TOKEN=16;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"PRINTLN", "PRINT", "LETTER", "DIGIT", "EOL", "IDENT", "POSITIVE_DIGIT", 
			"INT", "OBRACE", "CBRACE", "SEMI", "STRING_CAR", "STRING", "MULTI_LINE_STRING", 
			"COMMENT", "MULTI_LINE_COMMENT", "DUMMY_TOKEN"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'println'", "'print'", null, null, "'\n'", null, null, null, "'('", 
			"')'", "';'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "PRINTLN", "PRINT", "LETTER", "DIGIT", "EOL", "IDENT", "POSITIVE_DIGIT", 
			"INT", "OBRACE", "CBRACE", "SEMI", "STRING", "MULTI_LINE_STRING", "COMMENT", 
			"MULTI_LINE_COMMENT", "DUMMY_TOKEN"
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
		case 14:
			COMMENT_action((RuleContext)_localctx, actionIndex);
			break;
		case 15:
			MULTI_LINE_COMMENT_action((RuleContext)_localctx, actionIndex);
			break;
		}
	}
	private void COMMENT_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:
			 skip(); 
			break;
		}
	}
	private void MULTI_LINE_COMMENT_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 1:
			 skip(); 
			break;
		}
	}
<<<<<<< HEAD

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\22\u0090\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4"+
		"\3\5\3\5\3\6\3\6\3\7\3\7\5\7<\n\7\3\7\3\7\3\7\7\7A\n\7\f\7\16\7D\13\7"+
		"\3\b\3\b\3\t\3\t\3\t\7\tK\n\t\f\t\16\tN\13\t\5\tP\n\t\3\n\3\n\3\13\3\13"+
		"\3\f\3\f\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\7\16`\n\16\f\16\16\16c"+
		"\13\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\17\3\17\7\17n\n\17\f\17\16"+
		"\17q\13\17\3\17\3\17\3\20\3\20\3\20\3\20\7\20y\n\20\f\20\16\20|\13\20"+
		"\3\20\3\20\3\21\3\21\3\21\3\21\3\21\7\21\u0085\n\21\f\21\16\21\u0088\13"+
		"\21\3\21\3\21\3\21\3\21\3\21\3\22\3\22\2\2\23\3\3\5\4\7\5\t\6\13\7\r\b"+
		"\17\t\21\n\23\13\25\f\27\r\31\2\33\16\35\17\37\20!\21#\22\3\2\5\4\2C\\"+
		"c|\4\2&&aa\3\2\f\f\2\u009e\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2"+
		"\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2"+
		"\25\3\2\2\2\2\27\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2"+
		"\2\2\2#\3\2\2\2\3%\3\2\2\2\5-\3\2\2\2\7\63\3\2\2\2\t\65\3\2\2\2\13\67"+
		"\3\2\2\2\r;\3\2\2\2\17E\3\2\2\2\21O\3\2\2\2\23Q\3\2\2\2\25S\3\2\2\2\27"+
		"U\3\2\2\2\31W\3\2\2\2\33Y\3\2\2\2\35f\3\2\2\2\37t\3\2\2\2!\177\3\2\2\2"+
		"#\u008e\3\2\2\2%&\7r\2\2&\'\7t\2\2\'(\7k\2\2()\7p\2\2)*\7v\2\2*+\7n\2"+
		"\2+,\7p\2\2,\4\3\2\2\2-.\7r\2\2./\7t\2\2/\60\7k\2\2\60\61\7p\2\2\61\62"+
		"\7v\2\2\62\6\3\2\2\2\63\64\t\2\2\2\64\b\3\2\2\2\65\66\4\62;\2\66\n\3\2"+
		"\2\2\678\7\f\2\28\f\3\2\2\29<\5\7\4\2:<\t\3\2\2;9\3\2\2\2;:\3\2\2\2<B"+
		"\3\2\2\2=A\5\7\4\2>A\5\t\5\2?A\t\3\2\2@=\3\2\2\2@>\3\2\2\2@?\3\2\2\2A"+
		"D\3\2\2\2B@\3\2\2\2BC\3\2\2\2C\16\3\2\2\2DB\3\2\2\2EF\4\63;\2F\20\3\2"+
		"\2\2GP\7\62\2\2HL\5\17\b\2IK\5\t\5\2JI\3\2\2\2KN\3\2\2\2LJ\3\2\2\2LM\3"+
		"\2\2\2MP\3\2\2\2NL\3\2\2\2OG\3\2\2\2OH\3\2\2\2P\22\3\2\2\2QR\7*\2\2R\24"+
		"\3\2\2\2ST\7+\2\2T\26\3\2\2\2UV\7=\2\2V\30\3\2\2\2WX\13\2\2\2X\32\3\2"+
		"\2\2Ya\7$\2\2Z`\5\31\r\2[\\\7^\2\2\\`\7$\2\2]^\7^\2\2^`\7^\2\2_Z\3\2\2"+
		"\2_[\3\2\2\2_]\3\2\2\2`c\3\2\2\2a_\3\2\2\2ab\3\2\2\2bd\3\2\2\2ca\3\2\2"+
		"\2de\7$\2\2e\34\3\2\2\2fo\7$\2\2gn\5\31\r\2hn\5\13\6\2ij\7^\2\2jn\7$\2"+
		"\2kl\7^\2\2ln\7^\2\2mg\3\2\2\2mh\3\2\2\2mi\3\2\2\2mk\3\2\2\2nq\3\2\2\2"+
		"om\3\2\2\2op\3\2\2\2pr\3\2\2\2qo\3\2\2\2rs\7$\2\2s\36\3\2\2\2tu\7\61\2"+
		"\2uv\7\61\2\2vz\3\2\2\2wy\n\4\2\2xw\3\2\2\2y|\3\2\2\2zx\3\2\2\2z{\3\2"+
		"\2\2{}\3\2\2\2|z\3\2\2\2}~\b\20\2\2~ \3\2\2\2\177\u0080\7\61\2\2\u0080"+
		"\u0081\7,\2\2\u0081\u0086\3\2\2\2\u0082\u0085\5\31\r\2\u0083\u0085\5\13"+
		"\6\2\u0084\u0082\3\2\2\2\u0084\u0083\3\2\2\2\u0085\u0088\3\2\2\2\u0086"+
		"\u0084\3\2\2\2\u0086\u0087\3\2\2\2\u0087\u0089\3\2\2\2\u0088\u0086\3\2"+
		"\2\2\u0089\u008a\7,\2\2\u008a\u008b\7\61\2\2\u008b\u008c\3\2\2\2\u008c"+
		"\u008d\b\21\3\2\u008d\"\3\2\2\2\u008e\u008f\13\2\2\2\u008f$\3\2\2\2\17"+
		"\2;@BLO_amoz\u0084\u0086\4\3\20\2\3\21\3";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
=======
>>>>>>> 19d509610514971a96fd7f4d83c7d315fe857ed1
