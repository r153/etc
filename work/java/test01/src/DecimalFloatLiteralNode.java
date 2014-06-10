class DecimalFloatLiteralNode extends LiteralNode{
		double literal;
	public DecimalFloatLiteralNode(double literal){
		super(null,null);			
		this.literal = literal;
	}
	String dump(){ return super.dump()+":"+Double.toString(literal);}
}
