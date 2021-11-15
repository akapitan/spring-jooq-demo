package com.akapitan.demo.springjooqddd.domain.minion;

import java.util.HashMap;
import java.util.Map;
import org.jooq.tools.json.JSONObject;

public class Description {

  public static final String JSON_PERSONALITY = "personality";
  private static final String JSON_APPEARANCE = "apperance";
  Map<String, String> appearance = new HashMap<>();
  Map<String, String> personality = new HashMap<>();

  public JSONObject toJson() {
    JSONObject json = new JSONObject();

    json.put(JSON_APPEARANCE, this.appearance);
    json.put(JSON_PERSONALITY, this.personality);

    return json;
  }
}
