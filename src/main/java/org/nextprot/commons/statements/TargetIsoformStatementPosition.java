package org.nextprot.commons.statements;

public class TargetIsoformStatementPosition implements Comparable<TargetIsoformStatementPosition>{

	private String isoformAccession;
	private String specificity;
	private Integer begin;
	private Integer end;
	private String name;
	
	public TargetIsoformStatementPosition() {
		
	}

	// FIXME: WTF are the positions ????
	public TargetIsoformStatementPosition(String isoformAccession, String specificity, String name) {
		this.isoformAccession = isoformAccession;
		this.specificity = specificity;
		this.name = name;
	}

	public TargetIsoformStatementPosition(String isoformAccession, Integer begin, Integer end, String specificity, String name) {
		this.isoformAccession = isoformAccession;
		this.specificity = specificity;
		this.begin = begin;
		this.end = end;
		this.name = name;
	}
	
	public String getIsoformAccession() {
		return isoformAccession;
	}

	public String getSpecificity() {
		return specificity;
	}
	
	public Integer getBegin() {
		return begin;
	}
	public Integer getEnd() {
		return end;
	}

	@Override
	public int compareTo(TargetIsoformStatementPosition o) {
		return isoformAccession.compareTo(o.isoformAccession);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

}
