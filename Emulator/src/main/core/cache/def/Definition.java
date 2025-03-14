package core.cache.def;

import core.game.node.Node;
import core.tools.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Definition.
 *
 * @param <T> the type parameter
 */
public class Definition<T extends Node> {
    /**
     * The Id.
     */
    protected int id;
    /**
     * The Name.
     */
    protected String name = "null";
    /**
     * The Examine.
     */
    protected String examine;
    /**
     * The Options.
     */
    protected String[] options;
    /**
     * The Handlers.
     */
    protected final Map<String, Object> handlers = new HashMap<String, Object>();

    /**
     * Instantiates a new Definition.
     */
    public Definition() {

	}

    /**
     * Has options boolean.
     *
     * @return the boolean
     */
    public boolean hasOptions() {
		return hasOptions(true);
	}

    /**
     * Has options boolean.
     *
     * @param examine the examine
     * @return the boolean
     */
    public boolean hasOptions(boolean examine) {
		if (name.equals("null") || options == null) {
			return false;
		}
		for (String option : options) {
			if (option != null && !option.equals("null")) {
				if (examine || !option.equals("Examine")) {
					return true;
				}
			}
		}
		return false;
	}

    /**
     * Gets configuration.
     *
     * @param <V> the type parameter
     * @param key the key
     * @return the configuration
     */
    @SuppressWarnings("unchecked")
	public <V> V getConfiguration(String key) {
		return (V) handlers.get(key);
	}

    /**
     * Gets configuration.
     *
     * @param <V>  the type parameter
     * @param key  the key
     * @param fail the fail
     * @return the configuration
     */
    @SuppressWarnings("unchecked")
	public <V> V getConfiguration(String key, V fail) {
		V object = (V) handlers.get(key);
		if (object == null) {
			return fail;
		}
		return object;
	}

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
		return id;
	}

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
		this.id = id;
	}

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
		return name;
	}

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
		this.name = name;
	}

    /**
     * Gets examine.
     *
     * @return the examine
     */
    public String getExamine() {
		if (examine == null) {
			try {
				if(handlers.get("examine") != null)
					examine = handlers.get("examine").toString();
			} catch (Exception e){
				e.printStackTrace();
			}
			if(examine == null) {
				if (name.length() > 0) {
					examine = "It's a" + (StringUtils.isPlusN(name) ? "n " : " ") + name + ".";
				} else {
					examine = "null";
				}
			}
		}
		return examine;
	}

    /**
     * Sets examine.
     *
     * @param examine the examine
     */
    public void setExamine(String examine) {
		this.examine = examine;
	}

    /**
     * Get options string [ ].
     *
     * @return the string [ ]
     */
    public String[] getOptions() {
		return options;
	}

    /**
     * Sets options.
     *
     * @param options the options
     */
    public void setOptions(String[] options) {
		this.options = options;
	}

    /**
     * Gets handlers.
     *
     * @return the handlers
     */
    public Map<String, Object> getHandlers() {
		return handlers;
	}

}