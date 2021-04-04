import './App.css';
import Canvas from './Canvas.js';
import CheckIn from './CheckIn.js';

function App() {

  return (
    <div class="flex-container">
        <div class="flex-item">
            <h1> Maps 3</h1>
            <p> To clear a path once it's been calculated, interact with/move the map or press the "Find my Route" button </p>
            <Canvas />
        </div>
        <div class="flex-item">
            <h1> Maps 4</h1>
            <CheckIn />

        </div>
    </div>
  );

}

export default App;
