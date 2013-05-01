package org.mindinformatics.gwt.framework.component.pipelines.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.mindinformatics.gwt.framework.src.IApplication;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

import com.google.gwt.user.client.Window;

/**
 * Basic implementation of pipelines.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public abstract class APipeline implements ICommandCompleted, IParametersCache {
	
	public static final String EXECUTE = "Execute";
	public static final String SKIP = "Skip";
	
	protected IApplication _application;
	
	protected ArrayList<IStage> _stages =  new ArrayList<IStage>();
	protected Map<String, String> _params = new HashMap<String, String>();
	
	private long startTime;
	private long stageStartTime;
	private int currentStage = 0;
	private boolean terminated = false;
	
	public APipeline(IApplication application) {
		_application = application;
	}
	
	public void reset() {
		currentStage = 0;
		terminated = false;
	}
	
	public void addParam(String paramName, String paramValue) {
		_params.put(paramName, paramValue);
	}
	
	public String getParamValue(String paramName) {
		return _params.get(paramName);
	}
	
	public void removeParam(String paramName) {
		_params.remove(paramName);
	}
	
	public void start(Map<String, String> params) {
		startTime = System.currentTimeMillis();
		reset();
		_application.getLogger().debug(this, "Parametrizing pipeline");
		for(IStage stage: _stages) {
			String param = params.get(stage.getCommand().getClass().getName());
			if(param!=null && !Boolean.parseBoolean(param)) {
				stage.setExecutable(false);
			} else {
				stage.setExecutable(true);
			}
		}
		
		_application.getLogger().debug(this, "Starting pipeline");
		
		if(_stages.size()>0) {
			executeStage(currentStage);
		}	
	}
	
	private void executeStage(int currentStage) {
		stageStartTime = System.currentTimeMillis();
		try {
			String stageName = _stages.get(currentStage).getCommand().getClass().getName();
			if(_stages.get(currentStage).isExecutable()) {
				_application.getLogger().debug(this, "Executing pipeline stage " + stageName);
				_stages.get(currentStage).execute();
			} else {
				_application.getLogger().debug(this, "Skipping pipeline stage " + stageName);
				 notifyStageCompletion();
			}
		} catch(Exception e) {
			Window.alert(this.getClass().getName() + " APipeline.executeStage (" + currentStage + ") " + e.getMessage());
			_application.getLogger().exception(this, e.getMessage());
			notifyPipelineTermination(startTime);
		}
	}
	
	@Override
	public void notifyStageCompletion() {
		next();
	}
	
	public void next() {
		_application.getLogger().debug(this, "TERMINATED STAGE " + currentStage + " in " + (System.currentTimeMillis()-stageStartTime) + "ms");
		if(!terminated) { 
			if(_stages.size()>0 && _stages.size()>(++currentStage)) {
				_application.getLogger().debug(this, "EXECUTING STAGE " + currentStage);
				executeStage(currentStage);
			} else {
				terminated = true;
				notifyPipelineTermination(startTime);
			}
		} else {
			_application.getLogger().exception(this, "EXECUTING STAGE AFTER TERMINATION " + currentStage);
		}
	}
	
	public abstract String getPipelineName();
	public abstract void notifyPipelineTermination(long startTime);
}
