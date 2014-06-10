class DecimalLiteralNode extends LiteralNode{
		Integer literal;
	public DecimalLiteralNode(Integer literal){
		super(null,null);			
		this.literal = literal;
	}
	String dump(){ return super.dump()+":"+Integer.toString(literal);}
}
