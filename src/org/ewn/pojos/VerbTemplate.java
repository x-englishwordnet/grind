package org.ewn.pojos;

/**
 * Verb template (sents.vrb)
 * 
 * @author Bernard Bou
 */
public class VerbTemplate
{
	/**
	 * Template id
	 */
	public final int id;

	/**
	 * Template text
	 */
	public final String template;

	/**
	 * Constructor
	 * 
	 * @param id
	 *            template id
	 * @param template
	 *            template text
	 */
	private VerbTemplate(final int id, final String template)
	{
		this.id = id;
		this.template = template;
	}

	/**
	 * Parse from line
	 * 
	 * @param line
	 *            line
	 * @return verb template
	 */
	public static VerbTemplate parse(final String line)
	{
		final String[] fields = line.split("\\s+");
		final int id = Integer.parseInt(fields[0]);
		final String text = line.substring(fields[0].length() + 1);
		return new VerbTemplate(id, text);
	}

	@Override
	public String toString()
	{
		return "id=" + this.id + " template=" + this.template;
	}
}