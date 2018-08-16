package org.nextprot.commons.statements.constants;

public enum NextProtSource {

	BioEditor("neXtProt", "http://kant.sib.swiss:9001/bioeditor"),
	GlyConnect("GlyConnect", "http://kant.sib.swiss:9001/glyconnect")
    ;
	

	private String sourceName;
	private String statementsUrl;
	
	NextProtSource(String sourceName, String statementsUrl){
		this.sourceName = sourceName;
		this.statementsUrl = statementsUrl;
	}
	
	public String getSourceName() {
		return sourceName;
	}

	public String getStatementsUrl() {
		return statementsUrl;
	}

}
