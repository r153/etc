class BooleanLiteralNode extends LiteralNode{
		boolean literal;
	public BooleanLiteralNode(boolean literal){
		super(null,null);			
		this.literal = literal;
	}
	String dump(){ return super.dump()+":"+Boolean.toString(literal);}

}
