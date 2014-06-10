
abstract class Node{
	protected final Node left;
	protected final Node right;

	abstract String dump();

	String show(){
		String dumpL="";
		String dumpR="";

		if(left!=null)	dumpL	=left.show();
		if(right!=null)	dumpR	=left.show();
			
		return "[" + dumpL + "," + dump() + "," + dumpR + "]" ; 
	}

	Node(Node left,Node right){
		this.left	=left;
		this.right	=right;
	}

	//	class Num extends Node{
//		final String number;
//
//		Num(final String v){ number = v; }
//		String show(){ return number; }
//	}
//
//	class Op extends Node{
//		final Node leftArg;
//		final char operator;
//		final Node rightArg;
//
//		Op(final Node left, char op, final Node right){
//			leftArg = left;
//			operator = op;
//			rightArg = right;
//		}
//
//		String show(){ return "[" + leftArg.show() + operator + rightArg.show() + "]" ; }
//	}

	class ReferenceNode extends Node{
		ReferenceNode(final IdentifierNode left, final IdentifierNode right){
			super(left,right);
		}
		ReferenceNode(final IdentifierNode left, final MethodNode right){
			super(left,right);
		}
		String dump(){ return "REFERENCE";}
	}

	class IdentifierNode extends Node{
		String word;
		IdentifierNode(final Node left, String word, final Node right){
			super(null,null);			
			this.word = word;
		}
		String dump(){ return word;}
	}

	class MethodNode extends Node{
			String CALL="CALL";
		MethodNode(final IdentifierNode left,  final ArgArrayNode right){
			super(left,right);			
		}
		String dump(){ return CALL;}
	}

	class ArgArrayNode extends Node {
			String ARG="ARGUMENT";
			ArgArrayNode(final Node left,  final ArgArrayNode right){
			super(left,right);			
		}
		String dump(){ return ARG;}
	}

	class ParameterNode extends Node {
		ParameterNode(final ReferenceNode right){
			super(null,right);
		}
		ParameterNode(final LiteralNode right){
			super(null,right);
		}
		String dump(){ return "PARAM";}
	}

//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------


}