<#assign content>

  <br> <br><br><br><br>
<div class = "content-block">
<h1> We're going on a trip in our favorite rocket ship! </h1>
  <h1> Query for stars!</h1>

  <style>
    .flex-container{
      display: flex;
      flex-direction: row;
      flex-wrap: nowrap;
      justify-content: space-around;
      align-items: center;
      text-align: center;
    }
  </style>

    <form method="GET" action="/neighbors">
      <div class="flex-container center">
        <div class="item1">
          <input type="radio" name="naiveradio" value="naive" checked>
          naive
          <br>
          <input type="radio" name="naiveradio" value="non-naive">
          non-naive
        </div>
        <div class="item2">
          <input type="radio" name="commandradio" value="neighbors" checked>
          neighbors
          <br>
          <input type="radio" name="commandradio" value="radius" checked>
          radius
        </div>
      </div>
      <br>
      <label for="text">Enter r/k x y z OR r/k "name": </label><br>
      <textarea name="text" id="text"></textarea>
      <br>
      <input type="submit">
    </form>

${answer}

</div>


</#assign>
<#include "main.ftl">