package org.nextprot.commons.statements;

import org.nextprot.commons.statements.specs.StatementField;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StatementSet{

	private LinkedHashSet<Statement> statements;

	public StatementSet(Collection<Statement> statements){
		this.statements = new LinkedHashSet<>(statements);
	}

	public boolean contains(Statement statement) {
		return statements.contains(statement);
	}

	public boolean isEmpty() {
		return statements.isEmpty();
	}

	public int count() {
		return statements.size();
	}

	public Set<Statement> getStatements() {
		return statements;
	}

	public Stream<Statement> stream() {
		return statements.stream();
	}

	public void forEach(Consumer<Statement> action) {
		statements.forEach(action);
	}

	// Utility
	public Map<String, Long> countByField(StatementField field){
		return statements.stream().map(s -> s.getValue(field))
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

	}
	public StatementSet filter(StatementField field, String ... possibleValues){
		Set<String> possibleValuesSet = new LinkedHashSet<>(Arrays.asList(possibleValues));
		return new StatementSet(statements.stream()
				.filter(s -> possibleValuesSet.contains(s.getValue(field)))
				.collect(Collectors.toSet()));
	}

	public StatementSet filterNot(StatementField field, String value) {
		return new StatementSet(statements.stream()
				.filter(s -> !value.equals(s.getValue(field)))
				.collect(Collectors.toSet()));
	}


}
