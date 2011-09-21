package vanilla.java.collections.comparison;

import java.text.MessageFormat;

/**
 * A simple container for a Collection operation.
 *
 * @author c.cerbo
 */
public abstract class Operation {

	private String descriptionPattern;
	protected long iterations;

	public Operation(String descriptionPattern, long iterations) {
		this.descriptionPattern = descriptionPattern;
		this.iterations = iterations;
	}

	public String getDescription() {
		return MessageFormat.format(descriptionPattern, iterations);
	}

	/**
	 * Execute the operation and return the collection object.
	 *
	 * @return the collection object.
	 */
	public abstract Object execute() throws Exception;
}
