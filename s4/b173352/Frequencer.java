package s4.b173352; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
import java.lang.*;
import s4.specification.*;

/*
interface FrequencerInterface {     // This interface provides the design for frequency counter.
    void setTarget(byte[]  target); // set the data to search.
    void setSpace(byte[]  space);  // set the data to be searched target from.
    int frequency(); //It return -1, when TARGET is not set or TARGET's length is zero
                    //Otherwise, it return 0, when SPACE is not set or Space's length is zero
                    //Otherwise, get the frequency of TAGET in SPACE
    int subByteFrequency(int start, int end);
    // get the frequency of subByte of taget, i.e target[start], taget[start+1], ... , target[end-1].
    // For the incorrect value of START or END, the behavior is undefined.
*/


public class Frequencer implements FrequencerInterface{
    // Code to Test, *warning: This code  contains intentional problem*
    byte [] myTarget;
    byte [] mySpace;
    int [] suffixArray;

    boolean targetReady = false;
    boolean spaceReady = false;

    int suffixCount = 0;

    public void setTarget(byte [] target) {
	myTarget = target;
	if(myTarget.length>0) targetReady = true;
    }

    private void printSuffixArray() {
	if(spaceReady) {
	    for(int i=0; i< mySpace.length; i++) {
		int s = suffixArray[i];
		for(int j=s;j<mySpace.length;j++) {
		    System.out.write(mySpace[j]);
		}
		System.out.write('\n');
	    }
	}
	
    }
    
    private int suffixCompare(int i, int j) {
	int si = suffixArray[i];
	int sj = suffixArray[j];
	int s = 0;
	if(si > s) s = si;
	if(sj > s) s = sj;
	int n = mySpace.length - s;
	for(int k=0;k<n;k++) {
	    if(mySpace[si+k]>mySpace[sj+k]) return 1;
	    if(mySpace[si+k]<mySpace[sj+k]) return -1;
	}
	if(si < sj) return 1;
	if(si > sj) return -1;
	return 0;
	/*if(mySpace[i] > mySpace[j]){
	    return 1;
	}
	else if(mySpace[i] < mySpace[j]){
	    return -1;   
	}
	else{
	    int val;
	    val = suffixCompare(i+1,j+1);
	    return val;
	    }*/
    }
    
    
    public void setSpace(byte []space) { //mySpace = space;
	mySpace = space; if(mySpace.length>0)  spaceReady = true;
	suffixArray = new int[space.length];
	// put all suffixes in suffixArray. Each suffix is expressed by one interger.
	for(int i = 0; i< space.length; i++) {
	   suffixArray[i] = i;
	}
	//printSuffixArray();
	/*for(int i=0;i<space.length;i++){
	    for(int j=0;j<space.length-i-1;j++){
		int ans = suffixCompare(j,j+1);
		if(ans == 1){
		    int buff = suffixArray[j+1];
		    suffixArray[j+1] = suffixArray[j];
		    suffixArray[j] = buff;
		}
	    }
	    }*/
	quicksort(space,0,suffixArray.length-1);
	//quicksort(space,0,suffixArray.length-1);
	//printSuffixArray();
    }

    public void quicksort(byte []space, int start, int end){
	//int arraylength = start + end;
	//if(arraylength % 2 != 0)arraylength++;
	int p = start/*(arraylength) / 2*/;
	int more = start;
	int less = end;
	if(start < end){
	    while(true){
		while(suffixCompare(more,p) == -1){
		    more++;
		}
		while(suffixCompare(less,p) == 1){
		    less--;
		}
		if(more >= less){
		    break;
		}
		int buff = suffixArray[more];
		suffixArray[more] = suffixArray[less];
		suffixArray[less] = buff;
		//printSuffixArray();
		//System.out.println();
		more++;
		less--;
	    }
	    quicksort(space,start,more-1);
	    quicksort(space,less+1,end);
	}
    }

    private int targetCompare(int i, int start, int end) {
	if((i > mySpace.length) && (start < end))return -1;
	if((start == end) && (i <= mySpace.length)) return 0;
	if(mySpace[i]>myTarget[start]){
	    return 1;
	}else if(mySpace[i]<myTarget[start]){
	    return -1;
	}else{
	    if(start > end)return 0;
	    int ans = targetCompare(i+1,start+1,end);
	    return ans;
	}
    }

    private int subByteStartIndex(int start, int end, int arraystart, int arrayend){
	/*for(int i=0;i<mySpace.length;i++){
	    if(targetCompare(suffixArray[i],start,end)==0)return i;
	    }*/
	int arraylength = arraystart + arrayend;
	if(arraylength % 2 != 0)arraylength++;
	int mid = (arraylength) / 2;
	if(mid == 0)return 0;
	int midcomp = targetCompare(suffixArray[mid],start,end);
	int midprevcomp = targetCompare(suffixArray[mid-1],start,end);
	if(midcomp == 0 && midprevcomp == -1){
	    return mid;
	}else if(midcomp == 1 ||(midcomp == 0 && midprevcomp != -1)){
	    return subByteStartIndex(start,end,arraystart,mid-1);
	}else if(midcomp == -1){
	    return subByteStartIndex(start,end,mid+1,arrayend);
	}
	return suffixArray.length;
    }

    private int subByteEndIndex(int start, int end, int arraystart, int arrayend){
	/*for(int i=0;i<mySpace.length-1;i++){
	  if(targetCompare(suffixArray[i],start,end)==0 && targetCompare(suffixArray[i+1],start,end) != 0)
	      return i+1;
	      }*/
	int arraylength = arraystart + arrayend;
	if(arraylength % 2 != 0)arraylength++;
	int mid = (arraylength) / 2;
	if(mid == suffixArray.length)return suffixArray.length;
	int midcomp = targetCompare(suffixArray[mid],start,end);
	int midprevcomp = targetCompare(suffixArray[mid-1],start,end);
	if(midcomp == 1 && midprevcomp == 0){
	    return mid;
	}else if(midcomp == 1 && midprevcomp != 0){
	    return subByteEndIndex(start,end,arraystart,mid-1);
	}else if(midcomp != 1){
	    return subByteEndIndex(start,end,mid+1,arrayend);
	    }
	return suffixArray.length;
    }
    
    /* public int frequency() {
	int targetLength = myTarget.length;
	int spaceLength = mySpace.length;
	int count = 0;
	if(targetLength == 0)return -1;
	for(int start = 0; start<spaceLength; start++) { // Is it OK?
	    boolean abort = false;
	    for(int i = 0; i<targetLength; i++) {
		if(myTarget[i] != mySpace[start+i]) { abort = true; break; }
	    }
	    if(abort == false) { count++; }
	}
	return count;
	}*/
    public int frequency() {
	if(targetReady == false) return -1;
	if(spaceReady == false) return 0;
	return subByteFrequency(0, myTarget.length);
    }

    // I know that here is a potential problem in the declaration.
    public int subByteFrequency(int start, int end) { 
	// Not yet, but it is not currently used by anyone.
	/*int targetLength = myTarget.length;
	int spaceLength = mySpace.length;
	if(targetLength == 0)return -1;
	if(!(start >= 0 && start <= end && end < targetLength)) return -1;
	byte [] result = new byte[end - start];
	for(int i = 0; i<end - start; i++) { result[i] = myTarget[start + i]; };
	int count = 0;
	int resultLength = result.length;
	for(int head = 0; head<spaceLength; head++) { // Is it OK?
	    boolean abort = false;
	    for(int i = 0; i<resultLength; i++) {
		if(result[i] != mySpace[head+i]) { abort = true; break; }
	    }
	    if(abort == false) { count++; }
	}
	return count;*/
	int first = subByteStartIndex(start,end,0,suffixArray.length);
	int last1 = subByteEndIndex(start, end,0,suffixArray.length);
	//inspection code
	//for(int k=start;k<end;k++) { System.out.write(myTarget[k]); }
	//System.out.printf(": first=%d last1=%d\n", first, last1);
	
	return last1 - first;
    }

    public static void main(String[] args) {
	Frequencer myObject;
	int freq;
	try {
	    System.out.println("checking my Frequencer");
	    myObject = new Frequencer();
	    myObject.setSpace("Hi Ho Hi Ho".getBytes());
	    myObject.setTarget("H".getBytes());
	    freq = myObject.frequency();
	    System.out.print("\"H\" in \"Hi Ho Hi Ho\" appears "+freq+" times. ");
	    if(4 == freq) { System.out.println("OK"); } else {System.out.println("WRONG"); }
	}
	catch(Exception e) {
	    System.out.println("Exception occurred: STOP");
	}
    }
}	    
	    
