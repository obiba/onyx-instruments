/*******************************************************************************
 * Copyright (c) 2016 OBiBa. All rights reserved.
 *  
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *  
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.obiba.onyx.jade.instrument.ndd;

import java.util.Map;

public class CommandData {
  private String type;

  private Map<String, String> parameters;

  public CommandData() {
    super();
  }

  public String getType() {
    return type;
  }

  public Map<String, String> getParameters() {
    return parameters;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setParameters(Map<String, String> parameters) {
    this.parameters = parameters;
  }
}