import React, { useState, useEffect } from 'react';
import {
  NavLink,
} from 'react-router-dom';

function AppExample(): JSX.Element {
  const [count, setCount] = useState(10);
  const [once] = useState(0);

  useEffect(() => {
    // NO effect for Server-side rendering
    setCount(11);
    Promise.resolve(12).then((v) => setCount(v));
  }, [once]);

  return (
    <>
      <ul>
        <li>
          <NavLink to="/home">Home</NavLink>
        </li>
        <li>
          <NavLink to="/app-example">App Example</NavLink>
        </li>
      </ul>
      <p>
        App Example
        {' '}
        {count}
      </p>
      <div data-spa-render="spaDynamicView1" />
      <div data-spa-render="spaDynamicView2" />
    </>
  );
}

export default AppExample;
