import{useEffect}from 'react';

const useFetchPost =(url,data)=>{
const abortCont = new AbortController();


  useEffect(()=>{
      var row = JSON.stringify(data);
      requestOtions={
          method:"Post",
          rederict:"follow",
          body: row
      };
   fetch(url,requestOtions)
    .then(res => {
        if(!res.ok){
            throw Error ("couldn't fetch the data for the current resource");
        }
        return res.json();
    })
   
    .catch(err => {
        if(err.name==='AbortError'){
            console.log('fetched aborted')
        }else{
        setIsPending(false);
        setError(err.message);}
    })

return () => abortCont.abort();
},[url])

}
export default useFetch;