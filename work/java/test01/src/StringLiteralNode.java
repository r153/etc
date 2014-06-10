class StringLiteralNode extends LiteralNode{
		String literal;
	StringLiteralNode(String literal){
		super(null,null);			
		this.literal = literal;
	}

	String dump(){ return super.dump()+":"+literal;}
	
}

