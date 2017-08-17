xquery version "1.0-ml";

module namespace reverse = "http://marklogic.com/rest-api/resource/reverse-string";

declare default function namespace "http://www.w3.org/2005/xpath-functions";

declare option xdmp:mapping "false";

(: GET - reverses the string passed as a parameter :)
(: param string - a string to reverse :)
(: returns reversed input :)
declare function reverse:get(
  $context as map:map,
  $params  as map:map
) as document-node()*
{
  let $output-types := map:put($context, "output-types", "application/json")

  (: http://www.xqueryfunctions.com/xq/functx_reverse-string.html :)
  return document {
    codepoints-to-string(reverse(string-to-codepoints(map:get($params, "string"))))
  }
};
