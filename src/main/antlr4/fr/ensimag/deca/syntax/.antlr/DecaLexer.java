// Generated from /user/9/soriarag/Projet_GL/src/main/antlr4/fr/ensimag/deca/syntax/DecaLexer.g4 by ANTLR 4.9.2
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
		LETTER=1, DIGIT=2, EOL=3, IDENT=4, POSITIVE_DIGIT=5, INT=6, OBRACE=7, 
		CBRACE=8, SEMI=9, STRING=10, MULTI_LINE_STRING=11, COMMENT=12, MULTI_LINE_COMMENT=13, 
		DUMMY_TOKEN=14;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"LETTER", "DIGIT", "EOL", "IDENT", "POSITIVE_DIGIT", "INT", "OBRACE", 
			"CBRACE", "SEMI", "STRING_CAR", "STRING", "MULTI_LINE_STRING", "COMMENT", 
			"MULTI_LINE_COMMENT", "DUMMY_TOKEN"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, "'\n'", null, null, null, "'('", "')'", "';'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "LETTER", "DIGIT", "EOL", "IDENT", "POSITIVE_DIGIT", "INT", "OBRACE", 
			"CBRACE", "SEMI", "STRING", "MULTI_LINE_STRING", "COMMENT", "MULTI_LINE_COMMENT", 
			"DUMMY_TOKEN"
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
		case 12:
			COMMENT_action((RuleContext)_localctx, actionIndex);
			break;
		case 13:
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

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\20z\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\3\2\3\2\3\3\3\3\3\4"+
		"\3\4\3\5\3\5\5\5*\n\5\3\5\3\5\3\5\7\5/\n\5\f\5\16\5\62\13\5\3\6\3\6\3"+
		"\7\3\7\3\7\3\7\5\7:\n\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\f"+
		"\3\f\3\f\3\f\7\fJ\n\f\f\f\16\fM\13\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3"+
		"\r\7\rX\n\r\f\r\16\r[\13\r\3\r\3\r\3\16\3\16\3\16\3\16\7\16c\n\16\f\16"+
		"\16\16f\13\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\7\17o\n\17\f\17\16\17"+
		"r\13\17\3\17\3\17\3\17\3\17\3\17\3\20\3\20\2\2\21\3\3\5\4\7\5\t\6\13\7"+
		"\r\b\17\t\21\n\23\13\25\2\27\f\31\r\33\16\35\17\37\20\3\2\5\4\2C\\c|\4"+
		"\2&&aa\3\2\f\f\2\u0087\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2"+
		"\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\27"+
		"\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\3!\3\2\2"+
		"\2\5#\3\2\2\2\7%\3\2\2\2\t)\3\2\2\2\13\63\3\2\2\2\r9\3\2\2\2\17;\3\2\2"+
		"\2\21=\3\2\2\2\23?\3\2\2\2\25A\3\2\2\2\27C\3\2\2\2\31P\3\2\2\2\33^\3\2"+
		"\2\2\35i\3\2\2\2\37x\3\2\2\2!\"\t\2\2\2\"\4\3\2\2\2#$\4\62;\2$\6\3\2\2"+
		"\2%&\7\f\2\2&\b\3\2\2\2\'*\5\3\2\2(*\t\3\2\2)\'\3\2\2\2)(\3\2\2\2*\60"+
		"\3\2\2\2+/\5\3\2\2,/\5\5\3\2-/\t\3\2\2.+\3\2\2\2.,\3\2\2\2.-\3\2\2\2/"+
		"\62\3\2\2\2\60.\3\2\2\2\60\61\3\2\2\2\61\n\3\2\2\2\62\60\3\2\2\2\63\64"+
		"\4\63;\2\64\f\3\2\2\2\65:\7\62\2\2\66\67\5\13\6\2\678\5\5\3\28:\3\2\2"+
		"\29\65\3\2\2\29\66\3\2\2\2:\16\3\2\2\2;<\7*\2\2<\20\3\2\2\2=>\7+\2\2>"+
		"\22\3\2\2\2?@\7=\2\2@\24\3\2\2\2AB\13\2\2\2B\26\3\2\2\2CK\7$\2\2DJ\5\25"+
		"\13\2EF\7^\2\2FJ\7$\2\2GH\7^\2\2HJ\7^\2\2ID\3\2\2\2IE\3\2\2\2IG\3\2\2"+
		"\2JM\3\2\2\2KI\3\2\2\2KL\3\2\2\2LN\3\2\2\2MK\3\2\2\2NO\7$\2\2O\30\3\2"+
		"\2\2PY\7$\2\2QX\5\25\13\2RX\5\7\4\2ST\7^\2\2TX\7$\2\2UV\7^\2\2VX\7^\2"+
		"\2WQ\3\2\2\2WR\3\2\2\2WS\3\2\2\2WU\3\2\2\2X[\3\2\2\2YW\3\2\2\2YZ\3\2\2"+
		"\2Z\\\3\2\2\2[Y\3\2\2\2\\]\7$\2\2]\32\3\2\2\2^_\7\61\2\2_`\7\61\2\2`d"+
		"\3\2\2\2ac\n\4\2\2ba\3\2\2\2cf\3\2\2\2db\3\2\2\2de\3\2\2\2eg\3\2\2\2f"+
		"d\3\2\2\2gh\b\16\2\2h\34\3\2\2\2ij\7\61\2\2jk\7,\2\2kp\3\2\2\2lo\5\25"+
		"\13\2mo\5\7\4\2nl\3\2\2\2nm\3\2\2\2or\3\2\2\2pn\3\2\2\2pq\3\2\2\2qs\3"+
		"\2\2\2rp\3\2\2\2st\7,\2\2tu\7\61\2\2uv\3\2\2\2vw\b\17\3\2w\36\3\2\2\2"+
		"xy\13\2\2\2y \3\2\2\2\16\2).\609IKWYdnp\4\3\16\2\3\17\3";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}